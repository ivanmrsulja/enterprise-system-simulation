package rs.enterprise.paymentserviceprovider.service.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import rs.enterprise.paymentserviceprovider.config.StaticApplicationContext;
import rs.enterprise.paymentserviceprovider.model.BitcoinWallet;
import rs.enterprise.paymentserviceprovider.model.CustomPayment;
import rs.enterprise.paymentserviceprovider.service.PaymentInterface;


public class BitcoinPaymentServiceImpl implements PaymentInterface {
    private BitcoinWallet bitcoinWallet;

    public BitcoinPaymentServiceImpl() {
        ApplicationContext context = StaticApplicationContext.getContext();
        assert context != null;
        System.out.println(context);
        this.bitcoinWallet = context.getBean(BitcoinWallet.class);
        System.out.println(this.bitcoinWallet);
    }

    @Override
    public String getPaymentServiceName() {
        return "bitcoin";
    }

    @Override
    public String createPayment(CustomPayment customPayment) {
        String businessCompanyWallet = "mohjSavDdQYHRYXcS3uS6ttaHP8amyvX78";
        this.bitcoinWallet.send(customPayment.getAmount().toString(),
                businessCompanyWallet);
        return "success";
    }

    @Override
    public String executePayment(String paymentId, String payerId) {
        return null;
    }

}
