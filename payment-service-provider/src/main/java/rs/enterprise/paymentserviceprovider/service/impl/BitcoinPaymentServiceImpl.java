package rs.enterprise.paymentserviceprovider.service.impl;

import com.paypal.api.payments.Payment;
import rs.enterprise.paymentserviceprovider.model.enums.PaymentIntent;
import rs.enterprise.paymentserviceprovider.model.enums.PaymentMethod;
import rs.enterprise.paymentserviceprovider.service.PaymentInterface;


public class BitcoinPaymentServiceImpl implements PaymentInterface {

    @Override
    public String getPaymentServiceName() {
        return "Bitcoin";
    }

//    @Override
//    public List<Payment> getPayments() {
//        return new ArrayList<>(List.of(new Payment[]{new Payment("EUR", 1000.00, LocalDate.of(2019, 12, 16)), new Payment("EUR", 5000.00, LocalDate.of(2022, 1, 30))}));
//    }

    @Override
    public Payment createPayment(Double total, String currency, PaymentMethod method, PaymentIntent intent, String description, String cancelUrl, String successUrl) {
        return null;
    }

    @Override
    public Payment executePayment(String paymentId, String payerId) {
        return null;
    }


}
