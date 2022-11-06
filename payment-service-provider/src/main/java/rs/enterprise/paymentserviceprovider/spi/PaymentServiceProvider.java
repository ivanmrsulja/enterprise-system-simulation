package rs.enterprise.paymentserviceprovider.spi;

import org.springframework.stereotype.Component;
import rs.enterprise.paymentserviceprovider.service.PaymentInterface;

@Component
public interface PaymentServiceProvider {

    PaymentInterface create();
}
