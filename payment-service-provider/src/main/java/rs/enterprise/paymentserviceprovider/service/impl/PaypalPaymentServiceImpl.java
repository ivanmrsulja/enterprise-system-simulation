package rs.enterprise.paymentserviceprovider.service.impl;

import rs.enterprise.paymentserviceprovider.model.Payment;
import rs.enterprise.paymentserviceprovider.service.PaymentInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaypalPaymentServiceImpl implements PaymentInterface {

    @Override
    public String getPaymentServiceName() {
        return "PayPal";
    }

    @Override
    public List<Payment> getPayments() {
        return new ArrayList<>(List.of(new Payment[]{new Payment("EUR", 100.00, LocalDate.now()), new Payment("EUR", 500.00, LocalDate.now())}));
    }
}
