package rs.enterprise.paymentserviceprovider.service.impl;

import rs.enterprise.paymentserviceprovider.model.BitcoinWallet;
import rs.enterprise.paymentserviceprovider.model.CustomPayment;
import rs.enterprise.paymentserviceprovider.service.PaymentInterface;


public class BitcoinPaymentServiceImpl implements PaymentInterface {

    private static final BitcoinWallet bitcoinWallet = new BitcoinWallet();

    @Override
    public String getPaymentServiceName() {
        return "Bitcoin";
    }

    @Override
    public String createPayment(CustomPayment customPayment) {
        bitcoinWallet.send(customPayment.getAmount().toString(),
                customPayment.getToBusinessCompanyWallet());
        return "success";
    }

    @Override
    public String executePayment(String paymentId, String payerId) {
        return null;
    }


}
