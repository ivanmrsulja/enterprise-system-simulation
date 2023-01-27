package rs.enterprise.paymentserviceprovider.service.impl;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import rs.enterprise.paymentserviceprovider.model.CustomPayment;
import rs.enterprise.paymentserviceprovider.model.enums.PaymentIntent;
import rs.enterprise.paymentserviceprovider.model.enums.PaymentMethod;
import rs.enterprise.paymentserviceprovider.service.PaymentInterface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PaypalPaymentServiceImpl implements PaymentInterface {

    private static final APIContext context =
            new APIContext("Aa0JWxXgo3_uoiXJ5xNwgeTzpcJGQDuB1rNFmLzyiKHu3gnKvucMuc7wVONNs6l63ryBfSebAoIq8kB9",
                    "EHxH7KLmTajeiluF_DMYSZagceLfvjv3Ibgt_hCHnoT6u4gF4JxqZCvu5t7fa6JkNAP35E5YNDRjRd_x",
                    "sandbox");;


    @Override
    public String getPaymentServiceName() {
        return "paypal";
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

//        APIContext context =
//                new APIContext("Aa0JWxXgo3_uoiXJ5xNwgeTzpcJGQDuB1rNFmLzyiKHu3gnKvucMuc7wVONNs6l63ryBfSebAoIq8kB9",
//                        "EHxH7KLmTajeiluF_DMYSZagceLfvjv3Ibgt_hCHnoT6u4gF4JxqZCvu5t7fa6JkNAP35E5YNDRjRd_x",
//                        "sandbox");;
        Payment createdPayment = payment.create(context);

        for(Links links : createdPayment.getLinks())
            if (links.getRel().equals("approval_url"))
                return  links.getHref();

        return "failed:/";
    }

    @Override
    public String executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(context, paymentExecution);

        if (executedPayment.getState().equals("approved"))
            return "<h1>Successful Transaction.</h1>";

//        http://www.bonita-sajt.com:8080/bonita/apps/userAppBonita/transaction-failed/
//        http://www.bonita-sajt.com:8080/bonita/apps/userAppBonita/transaction-successful/

        return "failed";
    }

}
