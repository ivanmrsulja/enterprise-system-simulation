package rs.enterprise.paymentserviceprovider.service;

import org.springframework.beans.factory.annotation.Autowired;
import rs.enterprise.paymentserviceprovider.spi.PaymentServiceFinder;

import java.util.HashSet;
import java.util.Set;

public class PaymentMethodService {

    private final PaymentServiceFinder paymentServiceFinder;

    @Autowired
    public PaymentMethodService(PaymentServiceFinder paymentServiceFinder) {
        this.paymentServiceFinder = paymentServiceFinder;
    }

    public Set<String> getAllAvaliablePaymentMethods() {
        var payments = new HashSet<String>();
        paymentServiceFinder.providers(true).forEachRemaining(provider -> {
            PaymentInterface paymentMethod = provider.create();

            payments.add(paymentMethod.getPaymentServiceName());
        });
        return payments;
    }
}
