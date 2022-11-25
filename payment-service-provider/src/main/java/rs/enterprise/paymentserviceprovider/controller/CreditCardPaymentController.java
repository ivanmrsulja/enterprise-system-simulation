package rs.enterprise.paymentserviceprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import rs.enterprise.paymentserviceprovider.clients.AcquirerBankClient;
import rs.enterprise.paymentserviceprovider.dto.AcquirerBankPaymentRequestDTO;
import rs.enterprise.paymentserviceprovider.dto.BankRedirectResponseDTO;
import rs.enterprise.paymentserviceprovider.dto.PSPRedirectResponseDTO;
import rs.enterprise.paymentserviceprovider.service.BankPaymentService;

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

    @GetMapping("/request-redirect-to-bank/{merchantOrderId}/{bankPaymentId}/{method}")
    public BankRedirectResponseDTO requestRedirectToBank(@PathVariable Long merchantOrderId, @PathVariable Integer bankPaymentId, @PathVariable String method) throws Exception {
        var paymentRequest = bankPaymentService.fetchBankPaymentRequest(merchantOrderId, bankPaymentId);
        var bankResponse =  acquirerBankClient.requestRedirect(apiKey, paymentRequest);
        bankResponse.setPaymentUrl(bankResponse.getPaymentUrl() + "/" + merchantOrderId + "/" + bankResponse.getPaymentId() + "/" + method);
        return bankResponse;
    }

    @PostMapping("/request-redirect") // ovo privremeno dok se ne cujemo kako izgleda API za PayPal i BTC pa da napravimo nesto genericko :)
    public PSPRedirectResponseDTO requestRedirect(@RequestBody AcquirerBankPaymentRequestDTO paymentRequest) throws Exception {
        return new PSPRedirectResponseDTO(bankPaymentService.createNewPaymentAndGenerateRedirectUrl(paymentRequest));
    }
}
