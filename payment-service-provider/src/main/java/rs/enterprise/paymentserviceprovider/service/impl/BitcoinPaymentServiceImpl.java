package rs.enterprise.paymentserviceprovider.service.impl;

import org.springframework.context.ApplicationContext;
import rs.enterprise.paymentserviceprovider.config.StaticApplicationContext;
import rs.enterprise.paymentserviceprovider.model.BitcoinWallet;
import rs.enterprise.paymentserviceprovider.model.CustomPayment;
import rs.enterprise.paymentserviceprovider.model.enums.TransactionState;
import rs.enterprise.paymentserviceprovider.service.BankPaymentService;
import rs.enterprise.paymentserviceprovider.service.PaymentInterface;


public class BitcoinPaymentServiceImpl implements PaymentInterface {
    private final BitcoinWallet bitcoinWallet;
    private final BankPaymentService bankPaymentService;

    public BitcoinPaymentServiceImpl() {
        ApplicationContext context = StaticApplicationContext.getContext();
        assert context != null;
        this.bitcoinWallet = context.getBean(BitcoinWallet.class);
        this.bankPaymentService = context.getBean(BankPaymentService.class);
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
        bankPaymentService.setTransactionState(customPayment.getMerchantOrderId(), TransactionState.SUCCESS);
        return "success";
    }

    @Override
    public String executePayment(String paymentId, String payerId) {
        return null;
    }

    @Override
    public String createSubscription(CustomPayment customPayment) throws Exception {
        return null;
    }

    @Override
    public String executeSubscription(String token) throws Exception {
        return null;
    }

}
