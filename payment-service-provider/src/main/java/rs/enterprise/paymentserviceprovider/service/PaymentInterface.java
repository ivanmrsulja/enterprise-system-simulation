package rs.enterprise.paymentserviceprovider.service;

import org.springframework.stereotype.Component;
import rs.enterprise.paymentserviceprovider.model.CustomPayment;


@Component
public interface PaymentInterface {

    String getPaymentServiceName();

    String createPayment(CustomPayment customPayment) throws Exception;

    String executePayment(String paymentId, String payerId) throws Exception;
}
