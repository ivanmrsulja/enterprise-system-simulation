package rs.enterprise.paymentserviceprovider.service;

import com.paypal.base.rest.PayPalRESTException;
import org.springframework.stereotype.Component;
import com.paypal.api.payments.Payment;
import rs.enterprise.paymentserviceprovider.model.enums.PaymentIntent;
import rs.enterprise.paymentserviceprovider.model.enums.PaymentMethod;


@Component
public interface PaymentInterface {

    String getPaymentServiceName();

    //List<Payment> getPayments();

    Payment createPayment(Double total, String currency, PaymentMethod method,
                          PaymentIntent intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException;


    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
}
