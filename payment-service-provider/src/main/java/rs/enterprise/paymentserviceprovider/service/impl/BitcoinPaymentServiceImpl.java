package rs.enterprise.paymentserviceprovider.service.impl;

import rs.enterprise.paymentserviceprovider.model.Payment;
import rs.enterprise.paymentserviceprovider.service.PaymentInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BitcoinPaymentServiceImpl implements PaymentInterface {

    @Override
    public String getPaymentServiceName() {
        return "Bitcoin";
    }

    @Override
    public List<Payment> getPayments() {
        return new ArrayList<>(List.of(new Payment[]{new Payment("EUR", 1000.00, LocalDate.of(2019, 12, 16)), new Payment("EUR", 5000.00, LocalDate.of(2022, 1, 30))}));
    }
}
