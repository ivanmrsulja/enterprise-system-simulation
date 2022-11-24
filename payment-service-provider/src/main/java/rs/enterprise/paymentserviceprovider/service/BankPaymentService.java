package rs.enterprise.paymentserviceprovider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.enterprise.paymentserviceprovider.dto.AcquirerBankPaymentRequestDTO;
import rs.enterprise.paymentserviceprovider.exception.NotFoundException;
import rs.enterprise.paymentserviceprovider.model.BankPayment;
import rs.enterprise.paymentserviceprovider.repository.BankPaymentRepository;

@Service
public class BankPaymentService {

    private final BankPaymentRepository bankPaymentRepository;

    @Autowired
    public BankPaymentService(BankPaymentRepository bankPaymentRepository) {
        this.bankPaymentRepository = bankPaymentRepository;
    }

    public String createNewPaymentAndGenerateRedirectUrl(AcquirerBankPaymentRequestDTO paymentRequest) {
        var savedPayment = bankPaymentRepository.save(
                new BankPayment(
                        paymentRequest.getMerchantId(),
                        paymentRequest.getMerchantPassword(),
                        paymentRequest.getMerchantOrderId(),
                        paymentRequest.getMerchantTimestamp(),
                        paymentRequest.getAmount(),
                        paymentRequest.getSuccessUrl(),
                        paymentRequest.getFailedUrl(),
                        paymentRequest.getErrorUrl()));

        return "URL BASE/" + savedPayment.getMerchantOrderId() + "/" + savedPayment.getId();
    }

    public AcquirerBankPaymentRequestDTO fetchBankPaymentRequest(Long merchantOrderId, Integer bankPaymentId) {
        var bankPayment = bankPaymentRepository.getByIdAndMerchantOrderId(bankPaymentId, merchantOrderId).orElseThrow(() -> new NotFoundException("No such bank payment with given credentials."));
        return new AcquirerBankPaymentRequestDTO(bankPayment.getMerchantId(),
                bankPayment.getMerchantPassword(),
                bankPayment.getAmount(),
                bankPayment.getMerchantOrderId(),
                bankPayment.getMerchantTimestamp(),
                bankPayment.getSuccessUrl(),
                bankPayment.getFailedUrl(),
                bankPayment.getErrorUrl());
    }
}
