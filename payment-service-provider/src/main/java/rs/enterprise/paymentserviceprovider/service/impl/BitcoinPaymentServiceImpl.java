package rs.enterprise.paymentserviceprovider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import rs.enterprise.paymentserviceprovider.model.BitcoinWallet;
import rs.enterprise.paymentserviceprovider.model.CustomPayment;
import rs.enterprise.paymentserviceprovider.service.PaymentInterface;


public class BitcoinPaymentServiceImpl implements PaymentInterface {

    @Autowired
    private BitcoinWallet bitcoinWallet;

    @Override
    public String getPaymentServiceName() {
        return "Bitcoin";
    }

    @Override
    public String createPayment(CustomPayment customPayment) {
        bitcoinWallet.send(customPayment.getAmount().toString(),
                customPayment.getToBusinessCompanyWallet());
        return null;
    }

    @Override
    public String executePayment(String paymentId, String payerId) {
        return null;
    }


}
