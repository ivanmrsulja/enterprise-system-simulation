package rs.enterprise.paymentserviceprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import rs.enterprise.paymentserviceprovider.annotation.Log;
import rs.enterprise.paymentserviceprovider.clients.AcquirerBankClient;
import rs.enterprise.paymentserviceprovider.dto.*;
import rs.enterprise.paymentserviceprovider.service.BankPaymentService;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/bank-payment")
public class CreditCardPaymentController {

    private final AcquirerBankClient acquirerBankClient;
    private final BankPaymentService bankPaymentService;

    @Value("${API_KEY}")
    private String apiKey;

    @Autowired
    public CreditCardPaymentController(AcquirerBankClient acquirerBankClient,
                                       BankPaymentService bankPaymentService) {
        this.acquirerBankClient = acquirerBankClient;
        this.bankPaymentService = bankPaymentService;
    }

    @Log(message = "Requested redirect to bank.")
    @GetMapping("/request-redirect-to-bank/{merchantOrderId}/{bankPaymentId}/{method}")
    public BankRedirectResponseDTO requestRedirectToBank(HttpServletRequest request, @PathVariable Long merchantOrderId, @PathVariable Integer bankPaymentId, @PathVariable String method) throws Exception {
        var paymentRequest = bankPaymentService.fetchBankPaymentRequest(merchantOrderId, bankPaymentId);
        var bankResponse =  acquirerBankClient.requestRedirect(apiKey, paymentRequest);
        bankResponse.setPaymentUrl(bankResponse.getPaymentUrl() + "/" + merchantOrderId + "/" + bankResponse.getPaymentId() + "/" + method);
        return bankResponse;
    }

    @Log(message = "Requested redirect to PSP.")
    @PostMapping("/request-redirect")
    public PSPRedirectResponseDTO requestRedirect(HttpServletRequest request, @Valid @RequestBody AcquirerBankPaymentRequestDTO paymentRequest, @RequestHeader("X-Auth-Token") String authToken) throws Exception {
        return new PSPRedirectResponseDTO(bankPaymentService.createNewPaymentAndGenerateRedirectUrl(paymentRequest, authToken));
    }

    @Log(message = "Requested payment check.")
    @PostMapping("/check-payment")
    public PaymentCheckResponseDTO checkPayment(HttpServletRequest request, @Valid @RequestBody AcquirerBankPaymentRequestDTO paymentRequest) {
        var success = bankPaymentService.checkTransactionStatus(paymentRequest.getMerchantOrderId());
        if(success) {
            return new PaymentCheckResponseDTO("Payment Successful!");
        }
        return new PaymentCheckResponseDTO("Payment Unsuccessful!");
    }

    @Log(message = "Requested redirect to result page.")
    @PostMapping("/final-redirect")
    public PSPRedirectResponseDTO finalRedirect(HttpServletRequest request, @RequestHeader("X-Auth-Token") String requestApiKey, @RequestBody AcquirerBankFinalStepDTO finalStep) throws AuthenticationException {
        if(!requestApiKey.equals(apiKey)) {
            throw new AuthenticationException("Bad api key.");
        }
        return new PSPRedirectResponseDTO(bankPaymentService.handleFinalRedirect(finalStep.getTransactionState(), finalStep.getMerchantOrderId()));
    }
}
