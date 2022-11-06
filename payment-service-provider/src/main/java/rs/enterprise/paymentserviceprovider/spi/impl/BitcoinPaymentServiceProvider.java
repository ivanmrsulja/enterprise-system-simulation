package rs.enterprise.paymentserviceprovider.spi.impl;


import rs.enterprise.paymentserviceprovider.service.PaymentInterface;
import rs.enterprise.paymentserviceprovider.service.impl.BitcoinPaymentServiceImpl;
import rs.enterprise.paymentserviceprovider.spi.PaymentServiceProvider;

public class BitcoinPaymentServiceProvider implements PaymentServiceProvider {
    @Override
    public PaymentInterface create() {
        return new BitcoinPaymentServiceImpl();
    }
}
