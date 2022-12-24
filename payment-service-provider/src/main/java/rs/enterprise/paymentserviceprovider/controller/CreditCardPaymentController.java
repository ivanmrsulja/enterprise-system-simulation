package rs.enterprise.paymentserviceprovider.controller;

import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.enterprise.paymentserviceprovider.annotation.Log;
import rs.enterprise.paymentserviceprovider.clients.AcquirerBankClient;
import rs.enterprise.paymentserviceprovider.dto.AcquirerBankFinalStepDTO;
import rs.enterprise.paymentserviceprovider.dto.AcquirerBankPaymentRequestDTO;
import rs.enterprise.paymentserviceprovider.dto.BankRedirectResponseDTO;
import rs.enterprise.paymentserviceprovider.dto.PSPRedirectResponseDTO;
import rs.enterprise.paymentserviceprovider.service.BankPaymentService;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;

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
    public ResponseEntity<Void> requestRedirect(HttpServletRequest request, @Valid @RequestBody AcquirerBankPaymentRequestDTO paymentRequest) throws Exception {
//        return new PSPRedirectResponseDTO(bankPaymentService.createNewPaymentAndGenerateRedirectUrl(paymentRequest));
//        response.sendRedirect(bankPaymentService.createNewPaymentAndGenerateRedirectUrl(paymentRequest));
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(bankPaymentService.createNewPaymentAndGenerateRedirectUrl(paymentRequest)))
                .build();
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
