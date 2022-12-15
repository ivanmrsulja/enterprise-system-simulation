package rs.enterprise.paymentserviceprovider.spi;

import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.ServiceLoader;

@Component
public class PaymentServiceFinder {

    public Iterator<PaymentServiceProvider> providers(boolean refresh) {
        ServiceLoader<PaymentServiceProvider> loader = ServiceLoader.load(PaymentServiceProvider.class);
        if (refresh) {
            loader.reload();
        }
        return loader.iterator();
    }
}
