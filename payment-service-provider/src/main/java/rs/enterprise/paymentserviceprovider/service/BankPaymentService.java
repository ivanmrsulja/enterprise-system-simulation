package rs.enterprise.paymentserviceprovider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.enterprise.paymentserviceprovider.dto.AcquirerBankPaymentRequestDTO;
import rs.enterprise.paymentserviceprovider.exception.InvalidUsernameOrPasswordException;
import rs.enterprise.paymentserviceprovider.exception.NotFoundException;
import rs.enterprise.paymentserviceprovider.model.BankPayment;
import rs.enterprise.paymentserviceprovider.model.enums.TransactionState;
import rs.enterprise.paymentserviceprovider.repository.BankPaymentRepository;
import rs.enterprise.paymentserviceprovider.repository.MerchantRepository;
import rs.enterprise.paymentserviceprovider.util.EncryptionUtil;

@Service
public class BankPaymentService {

    private final BankPaymentRepository bankPaymentRepository;

    private final MerchantRepository merchantRepository;

    private final EncryptionUtil encryptionUtil;

    @Autowired
    public BankPaymentService(BankPaymentRepository bankPaymentRepository,
                              MerchantRepository merchantRepository,
                              EncryptionUtil encryptionUtil) {
        this.bankPaymentRepository = bankPaymentRepository;
        this.merchantRepository = merchantRepository;
        this.encryptionUtil = encryptionUtil;
    }

    public String createNewPaymentAndGenerateRedirectUrl(AcquirerBankPaymentRequestDTO paymentRequest, String authToken) throws Exception {
        var merchant = merchantRepository.findByMerchantId(paymentRequest.getMerchantId())
                .orElseThrow(() -> new NotFoundException("Merchant with given ID does not exist."));

        if(!merchant.getApiKey().equals(authToken)) {
            throw new InvalidUsernameOrPasswordException("Bad API key");
        }

        var savedPayment = bankPaymentRepository.save(
                new BankPayment(
                        paymentRequest.getMerchantId(),
                        encryptionUtil.encrypt(paymentRequest.getMerchantPassword()),
                        paymentRequest.getMerchantOrderId(),
                        paymentRequest.getMerchantTimestamp(),
                        paymentRequest.getAmount(),
                        paymentRequest.getSuccessUrl(),
                        paymentRequest.getFailedUrl(),
                        paymentRequest.getErrorUrl(),
                        null));

        return "http://127.0.0.1:5173/make-payment/" + savedPayment.getMerchantOrderId() + "/" + savedPayment.getId() + "/" + savedPayment.getMerchantId();
    }

    public AcquirerBankPaymentRequestDTO fetchBankPaymentRequest(Long merchantOrderId, Integer bankPaymentId) throws Exception {
        var bankPayment = bankPaymentRepository.getByIdAndMerchantOrderId(bankPaymentId, merchantOrderId)
                .orElseThrow(() -> new NotFoundException("No such bank payment with given credentials."));
        return new AcquirerBankPaymentRequestDTO(bankPayment.getMerchantId(),
                encryptionUtil.decrypt(bankPayment.getMerchantPassword()),
                bankPayment.getAmount(),
                bankPayment.getMerchantOrderId(),
                bankPayment.getMerchantTimestamp(),
                bankPayment.getSuccessUrl(),
                bankPayment.getFailedUrl(),
                bankPayment.getErrorUrl());
    }

    public String handleFinalRedirect(TransactionState state, Long merchantOrderId) {
        var bankPayment = bankPaymentRepository.getByMerchantOrderId(merchantOrderId)
                .orElseThrow(() -> new NotFoundException("No such bank payment with given credentials."));

        String redirectUrl = "";
        switch(state) {
            case SUCCESS:
                redirectUrl += bankPayment.getSuccessUrl();
                bankPayment.setState(TransactionState.SUCCESS);
                // Ovdje treba neki async call da se BPM obavijesti da je prosla uplata
                break;
            case ERROR:
                redirectUrl += bankPayment.getErrorUrl();
                bankPayment.setState(TransactionState.ERROR);
                break;
            case FAILED:
                redirectUrl += bankPayment.getFailedUrl();
                bankPayment.setState(TransactionState.FAILED);
                break;
        }

        bankPaymentRepository.save(bankPayment);
        return redirectUrl;
    }

    public boolean checkTransactionStatus(Long merchantOrderId) {
        boolean isSuccess = false;
        var bankPayment = bankPaymentRepository.getByMerchantOrderId(merchantOrderId)
                .orElseThrow(() -> new NotFoundException("No such bank payment with given credentials."));

        if(bankPayment.getState() == TransactionState.SUCCESS) {
            isSuccess = true;
//            bankPaymentRepository.delete(bankPayment);
        }

        return isSuccess;
    }
}
