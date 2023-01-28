package rs.enterprise.paymentserviceprovider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.enterprise.paymentserviceprovider.dto.BusinessAccountDTO;
import rs.enterprise.paymentserviceprovider.exception.NotFoundException;
import rs.enterprise.paymentserviceprovider.model.BusinessAccount;
import rs.enterprise.paymentserviceprovider.repository.MerchantRepository;
import rs.enterprise.paymentserviceprovider.spi.PaymentServiceFinder;

import java.util.HashSet;
import java.util.Set;

@Service
public class PaymentMethodService {

    private final PaymentServiceFinder paymentServiceFinder;

    private final MerchantRepository merchantRepository;

    @Autowired
    public PaymentMethodService(PaymentServiceFinder paymentServiceFinder,
                                MerchantRepository merchantRepository) {
        this.paymentServiceFinder = paymentServiceFinder;
        this.merchantRepository = merchantRepository;
    }

    public Set<String> getAllAvailablePaymentMethods() {
        var payments = new HashSet<String>();
        payments.add("Credit Card");
        payments.add("QR Code");
        paymentServiceFinder.providers(true).forEachRemaining(provider -> {
            PaymentInterface paymentMethod = provider.create();

            payments.add(paymentMethod.getPaymentServiceName());
        });
        return payments;
    }

    public Set<String> getAllAvailablePaymentMethodsForMerchant(String merchantId) {
        var merchant =  merchantRepository.findByMerchantId(merchantId)
                .orElseThrow(() -> new NotFoundException("Merchant with given merchant ID does not exist."));
        return merchant.getPaymentMethods();
    }

    public void setPaymentMethodsForMerchant(Integer merchantId, Set<String> paymentMethods) {
        var merchant =  merchantRepository.findById(merchantId)
                .orElseThrow(() -> new NotFoundException("Merchant with given ID does not exist."));
        merchant.setPaymentMethods(paymentMethods);

        merchantRepository.save(merchant);
    }

    public void setBusinessAccountForMerchant(BusinessAccountDTO businessAccountDTO)  {
        var merchant =  merchantRepository.findByMerchantId(businessAccountDTO.getMerchantId())
               .orElseThrow(() -> new NotFoundException("Merchant with given ID does not exist."));
        merchant.getAccounts().add(new BusinessAccount(businessAccountDTO.getPaymentMethod(), businessAccountDTO.getAccount()));

        merchantRepository.save(merchant);
    }
}
