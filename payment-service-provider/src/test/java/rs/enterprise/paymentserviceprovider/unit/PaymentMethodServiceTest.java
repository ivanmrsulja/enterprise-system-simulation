package rs.enterprise.paymentserviceprovider.unit;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import rs.enterprise.paymentserviceprovider.exception.NotFoundException;
import rs.enterprise.paymentserviceprovider.model.Authority;
import rs.enterprise.paymentserviceprovider.model.Merchant;
import rs.enterprise.paymentserviceprovider.repository.MerchantRepository;
import rs.enterprise.paymentserviceprovider.service.PaymentMethodService;
import rs.enterprise.paymentserviceprovider.spi.PaymentServiceFinder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PaymentMethodServiceTest {

    @Mock
    private PaymentServiceFinder paymentServiceFinder;

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private PaymentMethodService paymentMethodService;

    @Test
    void getAllAvailablePaymentMethodsForMerchant_calledWithValidParams_isSuccess() {
        // GIVEN
        var authority = new Authority("ROLE_MERCHANT");
        var merchant = new Merchant("id",
                "password",
                "key",
                "name",
                "email",
                authority);
        merchant.setPaymentMethods(new HashSet<>(List.of(new String[]{"CreditCard", "QRCode"})));
        doReturn(Optional.of(merchant)).when(merchantRepository).findByMerchantId(merchant.getMerchantId());

        // WHEN
        var methods = paymentMethodService.getAllAvailablePaymentMethodsForMerchant(merchant.getMerchantId());

        // THEN
        assertEquals(2, methods.size());
    }

    @Test
    void getAllAvailablePaymentMethodsForMerchant_calledWithNonExistingId_throwsException() {
        // GIVEN
        doThrow(NotFoundException.class).when(merchantRepository).findByMerchantId("id");

        // THEN
        assertThrows(NotFoundException.class, () ->
                paymentMethodService.getAllAvailablePaymentMethodsForMerchant("id"));
    }

    @Test
    void setPaymentMethodsForMerchant_calledWithValidParams_isSuccess() {
        // GIVEN
        var authority = new Authority("ROLE_MERCHANT");
        var merchant = new Merchant("id",
                "password",
                "key",
                "name",
                "email",
                authority);
        merchant.setId(1);
        doReturn(Optional.of(merchant)).when(merchantRepository).findById(merchant.getId());

        // WHEN
        paymentMethodService.setPaymentMethodsForMerchant(merchant.getId(),
                new HashSet<>(List.of(new String[]{"CreditCard", "QRCode"})));

        // THEN
        verify(merchantRepository, times(1)).save(merchant);
    }

    @Test
    void setPaymentMethodsForMerchant_calledWithNonExistingId_throwsException() {
        // GIVEN
        doThrow(NotFoundException.class).when(merchantRepository).findById(any());

        // THEN
        assertThrows(NotFoundException.class, () ->
                paymentMethodService.setPaymentMethodsForMerchant(1, new HashSet<>(List.of(new String[]{"CreditCard", "QRCode"}))));
    }
}
