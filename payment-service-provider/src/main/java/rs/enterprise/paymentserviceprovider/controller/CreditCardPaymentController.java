package rs.enterprise.paymentserviceprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.enterprise.paymentserviceprovider.clients.AcquirerBankClient;
import rs.enterprise.paymentserviceprovider.dto.AcquirerBankPaymentRequestDTO;
import rs.enterprise.paymentserviceprovider.dto.BankRedirectResponseDTO;

@RestController
@RequestMapping("/api/credit-card-payment")
public class CreditCardPaymentController {

    private final AcquirerBankClient acquirerBankClient;

    @Value("${API_KEY}")
    private String apiKey;

    @Autowired
    public CreditCardPaymentController(AcquirerBankClient acquirerBankClient) {
        this.acquirerBankClient = acquirerBankClient;
    }

    @PostMapping("/request-redirect") // ovo privremeno dok se ne cujemo kako izgleda API za PayPal i BTC pa da napravimo nesto genericko :)
    public BankRedirectResponseDTO requestRedirect(@RequestBody AcquirerBankPaymentRequestDTO paymentRequest) {
        return acquirerBankClient.requestRedirect(apiKey, paymentRequest);
    }
}
