package rs.enterprise.paymentserviceprovider.spi.impl;


import rs.enterprise.paymentserviceprovider.service.PaymentInterface;
import rs.enterprise.paymentserviceprovider.service.impl.PaypalPaymentServiceImpl;
import rs.enterprise.paymentserviceprovider.spi.PaymentServiceProvider;

public class PaypalPaymentServiceProvider implements PaymentServiceProvider {

    @Override
    public PaymentInterface create() {
        return new PaypalPaymentServiceImpl();
    }
}
