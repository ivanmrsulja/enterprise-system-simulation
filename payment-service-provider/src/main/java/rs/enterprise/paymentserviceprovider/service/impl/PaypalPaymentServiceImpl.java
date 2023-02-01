package rs.enterprise.paymentserviceprovider.service.impl;

import com.paypal.api.payments.Currency;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import rs.enterprise.paymentserviceprovider.model.CustomPayment;
import rs.enterprise.paymentserviceprovider.model.enums.PaymentIntent;
import rs.enterprise.paymentserviceprovider.model.enums.PaymentMethod;
import rs.enterprise.paymentserviceprovider.service.PaymentInterface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

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
        amount.setTotal(String.format("%.2f", customPayment.getAmount()).replace(',','.'));

        Transaction transaction = new Transaction();
        transaction.setDescription(customPayment.getDescription());
        transaction.setAmount(amount);

        Payee payee = new Payee();
        payee.setEmail(customPayment.getAccount());
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

        String url = "http://www.bonita-sajt.com:8080/bonita/apps/userAppBonita/transaction-failed/";

        if (executedPayment.getState().equals("approved"))
            url = "http://www.bonita-sajt.com:8080/bonita/apps/userAppBonita/transaction-successful/";

        return String.format("<html>\n" +
                "\n" +
                "<head>\n" +
                "    <title>Successful Transacition</title>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta http-equiv=\"refresh\" content=\"0.1; URL=%s\" />\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "    <p>This page has been moved. If you are not redirected within 3 seconds, click <a\n" +
                "            href=\"%s\">here</a> to go to the Bonita homepage.</p>\n" +
                "</body>\n" +
                "\n" +
                "</html>", url, url);
    }

    @Override
    public String createSubscription(CustomPayment customPayment) throws Exception {
        Date startDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        String formattedStartDate = sdf.format(startDate);

        Payer payer = new Payer();
        payer.setPaymentMethod(PaymentMethod.paypal.toString());

        Agreement agreement = new Agreement();
        Plan plan = createPlan(customPayment.getCancelUrl(), customPayment.getSuccessUrl(),
                customPayment.getAmount(), customPayment.getNumberOfMonths());

        Plan newPlan = new Plan();
        newPlan.setId(plan.getId());
        context.setMaskRequestId(true);

        agreement.setName("Premium shop payment");
        agreement.setStartDate(formattedStartDate);
        agreement.setPayer(payer);
        agreement.setDescription("sdadas");
        agreement.setPlan(newPlan);
        agreement = agreement.create(context);

        for(Links links : agreement.getLinks())
            if (links.getRel().equals("approval_url"))
                return  links.getHref();

        return "failed:/";
    }

    @Override
    public String executeSubscription(String token) throws Exception {
        Agreement agreement = Agreement.execute(context, token);
        String url = "http://www.bonita-sajt.com:8080/bonita/apps/userAppBonita/transaction-failed/";

        if (agreement.getState().equals("Active"))
            url = "http://www.bonita-sajt.com:8080/bonita/apps/userAppBonita/transaction-successful/";

        return String.format("<html>\n" +
                "\n" +
                "<head>\n" +
                "    <title>Successful Subscription</title>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta http-equiv=\"refresh\" content=\"0.1; URL=%s\" />\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "    <p>This page has been moved. If you are not redirected within 3 seconds, click <a\n" +
                "            href=\"%s\">here</a> to go to the Bonita homepage.</p>\n" +
                "</body>\n" +
                "\n" +
                "</html>", url, url);
    }

    private Plan createPlan(String cancelUrl, String returnUrl,
                            Double amount, Integer numberOfMonths) throws PayPalRESTException {
        Plan plan = new Plan();

        plan.setName("Basic Plan");
        plan.setDescription("Basic plan");
        plan.setType("fixed");

        PaymentDefinition paymentDefinition = new PaymentDefinition();
        paymentDefinition.setName("Regular Payments");
        paymentDefinition.setType("REGULAR");
        paymentDefinition.setFrequency("MONTH");
        paymentDefinition.setFrequencyInterval("1");
        //paymentDefinition.setCycles("12");
        paymentDefinition.setCycles(String.valueOf(numberOfMonths));

        Currency currency = new Currency();
        currency.setCurrency("USD");
        //currency.setValue("10");
        currency.setValue(String.format("%.2f",amount / numberOfMonths).replace(',','.'));
        paymentDefinition.setAmount(currency);

        ChargeModels chargeModels = new ChargeModels();
        chargeModels.setType("TAX");
        chargeModels.setAmount(new Currency().setCurrency("USD").setValue("0"));
        List<ChargeModels> chargeModelsList = new ArrayList<>();
        chargeModelsList.add(chargeModels);
        paymentDefinition.setChargeModels(chargeModelsList);

        List<PaymentDefinition> paymentDefinitionList = new ArrayList<>();
        paymentDefinitionList.add(paymentDefinition);
        plan.setPaymentDefinitions(paymentDefinitionList);

        MerchantPreferences merchantPreferences = new MerchantPreferences();
        merchantPreferences.setSetupFee(currency);
        merchantPreferences.setCancelUrl(cancelUrl);
        merchantPreferences.setReturnUrl(returnUrl);
        merchantPreferences.setMaxFailAttempts("0");
        merchantPreferences.setAutoBillAmount("YES");
        merchantPreferences.setInitialFailAmountAction("CONTINUE");
        plan.setMerchantPreferences(merchantPreferences);
//        plan.setState("ACTIVE");
        plan = plan.create(context);

//        plan.setState("ACTIVE");  //Change state of created plan to 'ACTIVE'
//        plan.update(context, new ArrayList<>());
//
        List<Patch> patchRequestList = new ArrayList<>();
        Map<String, String> value = new HashMap<>();
        value.put("state", "ACTIVE");

        Patch patch = new Patch();
        patch.setPath("/");
        patch.setValue(value);
        patch.setOp("replace");
        patchRequestList.add(patch);

        plan.update(context, patchRequestList);
        return plan;
    }
}
