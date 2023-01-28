package rs.enterprise.paymentserviceprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.enterprise.paymentserviceprovider.dto.AcquirerBankPaymentRequestDTO;
import rs.enterprise.paymentserviceprovider.model.BusinessAccount;
import rs.enterprise.paymentserviceprovider.model.CustomPayment;
import rs.enterprise.paymentserviceprovider.model.Merchant;
import rs.enterprise.paymentserviceprovider.service.BankPaymentService;
import rs.enterprise.paymentserviceprovider.service.MerchantService;
import rs.enterprise.paymentserviceprovider.service.PaymentInterface;
import rs.enterprise.paymentserviceprovider.spi.PaymentServiceFinder;
import rs.enterprise.paymentserviceprovider.util.URLBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentServiceFinder paymentServiceFinder;

    @Autowired
    private BankPaymentService bankPaymentService;

    @Autowired
    private MerchantService merchantService;

    public static final String PAYPAL_SUCCESS_URL = "/api/payments/success";
    public static final String PAYPAL_CANCEL_URL = "/api/payments/cancel";

    @Autowired
    public PaymentController(PaymentServiceFinder paymentServiceFinder) {
        this.paymentServiceFinder = paymentServiceFinder;
    }

    @PostMapping("/pay")
    public String pay(HttpServletRequest request, @RequestBody CustomPayment customPayment) throws Exception {
        String cancelUrl = URLBuilder.getBaseURL(request)  + PAYPAL_CANCEL_URL;
        String successUrl = URLBuilder.getBaseURL(request)  + PAYPAL_SUCCESS_URL;
        customPayment.setSuccessUrl(successUrl);
        customPayment.setCancelUrl(cancelUrl);

        // ovu logiku mogu prebaciti unutar servisa, ali onda moram dobavljati sve preko
        // statickog application contexta jer su dinamicki servisi
        // primjer walleta unutar bitcoin servisa, mrzilo me tako raditi pa sam nabio ovdje sve
        AcquirerBankPaymentRequestDTO temp = bankPaymentService.fetchBankPaymentRequest(customPayment.getMerchantOrderId(),
                customPayment.getTransactionId());
        customPayment.setAmount(temp.getAmount());
        Merchant m = merchantService.findMerchantByMerchantId(temp.getMerchantId());
        for (BusinessAccount ba: m.getAccounts())
            if (ba.getPaymentMethod().equals(customPayment.getPaymentMethod())) {
                customPayment.setAccount(ba.getAccount());
                break;
            }

        System.out.println(customPayment);

        AtomicReference<String> result = new AtomicReference<>("");
        paymentServiceFinder.providers(true).forEachRemaining(provider -> {
            PaymentInterface paymentMethod = provider.create();
            if (paymentMethod.getPaymentServiceName().equals(customPayment.getPaymentMethod())) {
                try {
                    result.set(paymentMethod.createPayment(customPayment));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return result.get();
    }

    // posebna metoda neophodna za paypal i samo se izvrsava u slucaju paypal placanja
    @GetMapping(value = "/success")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) throws Exception {
        AtomicReference<String> result = new AtomicReference<>("");
        paymentServiceFinder.providers(true).forEachRemaining(provider -> {
            PaymentInterface paymentMethod = provider.create();
            if (paymentMethod.getPaymentServiceName().equals("paypal")) {
                try {
                    result.set(paymentMethod.executePayment(paymentId, payerId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return result.get();
    }
}
