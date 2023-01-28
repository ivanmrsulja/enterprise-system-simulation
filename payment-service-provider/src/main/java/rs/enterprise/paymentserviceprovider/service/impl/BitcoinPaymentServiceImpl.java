package rs.enterprise.paymentserviceprovider.service.impl;

import org.springframework.context.ApplicationContext;
import rs.enterprise.paymentserviceprovider.config.StaticApplicationContext;
import rs.enterprise.paymentserviceprovider.model.BitcoinWallet;
import rs.enterprise.paymentserviceprovider.model.CustomPayment;
import rs.enterprise.paymentserviceprovider.service.PaymentInterface;


public class BitcoinPaymentServiceImpl implements PaymentInterface {
    private final BitcoinWallet bitcoinWallet;

    public BitcoinPaymentServiceImpl() {
        ApplicationContext context = StaticApplicationContext.getContext();
        assert context != null;
        this.bitcoinWallet = context.getBean(BitcoinWallet.class);
    }

    @Override
    public String getPaymentServiceName() {
        return "bitcoin";
    }

    @Override
    public String createPayment(CustomPayment customPayment) {
        // ZAKUCANA VRIJEDNOST ZA BITCOIN TRANSAKCIJU
        this.bitcoinWallet.send("0.00001",
                customPayment.getAccount());
        return "success";
    }

    @Override
    public String executePayment(String paymentId, String payerId) {
        return null;
    }

}
