package rs.enterprise.paymentserviceprovider.service;

import com.paypal.base.rest.PayPalRESTException;
import org.springframework.stereotype.Component;
import rs.enterprise.paymentserviceprovider.model.CustomPayment;


@Component
public interface PaymentInterface {

    String getPaymentServiceName();

    String createPayment(CustomPayment customPayment) throws PayPalRESTException;


    String executePayment(String paymentId, String payerId) throws PayPalRESTException;
}
