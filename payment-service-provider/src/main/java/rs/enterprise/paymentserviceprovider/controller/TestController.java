package rs.enterprise.paymentserviceprovider.controller;

import com.paypal.api.payments.Links;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.enterprise.paymentserviceprovider.model.enums.PaymentIntent;
import rs.enterprise.paymentserviceprovider.model.enums.PaymentMethod;
import rs.enterprise.paymentserviceprovider.service.impl.PaypalPaymentServiceImpl;
import com.paypal.api.payments.Payment;
import rs.enterprise.paymentserviceprovider.util.URLBuilder;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/test")
public class TestController {

    //TODO ovo izmjeniti i prilagoditi nakon odradjenog bitcoina
    @Autowired
    private PaypalPaymentServiceImpl paymentInterface;

    public static final String PAYPAL_SUCCESS_URL = "/api/test/pay/success";
    public static final String PAYPAL_CANCEL_URL = "/api/test/pay/cancel";


    @PostMapping(value = "pay")
    public String pay(HttpServletRequest request){
        String cancelUrl = URLBuilder.getBaseURL(request)  + PAYPAL_CANCEL_URL;
        String successUrl = URLBuilder.getBaseURL(request)  + PAYPAL_SUCCESS_URL;

        try {
            Payment payment = paymentInterface.createPayment(
                    4.00,
                    "USD",
                    PaymentMethod.paypal,
                    PaymentIntent.sale,
                    "payment description",
                    cancelUrl,
                    successUrl);
            for(Links links : payment.getLinks()){
                if(links.getRel().equals("approval_url")){
                    return "success:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            System.err.println(e.getMessage());
        }

        return "failed:/";
    }

    @GetMapping(value = "pay/success")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) throws PayPalRESTException {
        Payment payment = paymentInterface.executePayment(paymentId, payerId);
        if (payment.getState().equals("approved")) {
            return "success";
        }
        return "failed";
    }
}
