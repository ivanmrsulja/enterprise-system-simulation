package rs.enterprise.paymentserviceprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.enterprise.paymentserviceprovider.model.CustomPayment;
import rs.enterprise.paymentserviceprovider.service.PaymentInterface;
import rs.enterprise.paymentserviceprovider.spi.PaymentServiceFinder;
import rs.enterprise.paymentserviceprovider.util.URLBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentServiceFinder paymentServiceFinder;

    public static final String PAYPAL_SUCCESS_URL = "/api/payments/success";
    public static final String PAYPAL_CANCEL_URL = "/api/payments/cancel";

    @Autowired
    public PaymentController(PaymentServiceFinder paymentServiceFinder) {
        this.paymentServiceFinder = paymentServiceFinder;
    }

    @PostMapping("/pay")
    public String pay(HttpServletRequest request, @RequestBody CustomPayment customPayment) {
        String cancelUrl = URLBuilder.getBaseURL(request)  + PAYPAL_CANCEL_URL;
        String successUrl = URLBuilder.getBaseURL(request)  + PAYPAL_SUCCESS_URL;
        customPayment.setSuccessUrl(successUrl);
        customPayment.setCancelUrl(cancelUrl);
        customPayment.setToBusinessCompanyEmail("sb-43hmsz22738278@business.example.com");

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
            if (paymentMethod.getPaymentServiceName().equals("PayPal")) {
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
