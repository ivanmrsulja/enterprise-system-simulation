package rs.enterprise.paymentserviceprovider.service.impl;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import rs.enterprise.paymentserviceprovider.model.CustomPayment;
import rs.enterprise.paymentserviceprovider.model.enums.PaymentIntent;
import rs.enterprise.paymentserviceprovider.model.enums.PaymentMethod;
import rs.enterprise.paymentserviceprovider.service.PaymentInterface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PaypalPaymentServiceImpl implements PaymentInterface {

    @Autowired
    private APIContext apiContext;

    @Override
    public String getPaymentServiceName() {
        return "PayPal";
    }

    @Override
    public String createPayment(CustomPayment customPayment) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(customPayment.getCurrency());
        customPayment.setAmount(BigDecimal.valueOf(customPayment.getAmount())
                .setScale(2, RoundingMode.HALF_UP).doubleValue());
        amount.setTotal(String.format("%.2f", customPayment.getAmount()));

        Transaction transaction = new Transaction();
        transaction.setDescription(customPayment.getDescription());
        transaction.setAmount(amount);

        Payee payee = new Payee();
        payee.setEmail(customPayment.getToBusinessCompanyEmail());
        transaction.setPayee(payee);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(PaymentMethod.paypal.toString());

        Payment payment = new Payment();
        payment.setIntent(PaymentIntent.sale.toString());
        payment.setPayer(payer);

        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(customPayment.getCancelUrl());
        redirectUrls.setReturnUrl(customPayment.getSuccessUrl());
        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment = payment.create(apiContext);

        for(Links links : createdPayment.getLinks())
            if (links.getRel().equals("approval_url"))
                return "success:" + links.getHref();

        return "failed:/";
    }

    @Override
    public String executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(apiContext, paymentExecution);

        if (executedPayment.getState().equals("approved"))
            return "success";

        return "failed";
    }

}
