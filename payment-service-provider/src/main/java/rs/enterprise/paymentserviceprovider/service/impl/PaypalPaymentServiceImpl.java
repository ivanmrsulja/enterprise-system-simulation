package rs.enterprise.paymentserviceprovider.service.impl;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.enterprise.paymentserviceprovider.model.enums.PaymentIntent;
import rs.enterprise.paymentserviceprovider.model.enums.PaymentMethod;
import rs.enterprise.paymentserviceprovider.service.PaymentInterface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaypalPaymentServiceImpl implements PaymentInterface {


    @Autowired
    private APIContext apiContext;

    @Override
    public String getPaymentServiceName() {
        return "PayPal";
    }

//    @Override
//    public List<Payment> getPayments() {
//        return new ArrayList<>(List.of(new Payment[]{new Payment("EUR", 100.00, LocalDate.now()), new Payment("EUR", 500.00, LocalDate.now())}));
//    }

    @Override
    public Payment createPayment(Double total, String currency, PaymentMethod method, PaymentIntent intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        Payee payee = new Payee();
        payee.setEmail("sb-43hmsz22738278@business.example.com");
        transaction.setPayee(payee);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);

        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    @Override
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }



}
