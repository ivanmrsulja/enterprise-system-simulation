package rs.enterprise.paymentserviceprovider.service;

import org.springframework.stereotype.Component;
import rs.enterprise.paymentserviceprovider.model.Payment;

import java.util.List;

@Component
public interface PaymentInterface {

    String getPaymentServiceName();

    List<Payment> getPayments();
}
