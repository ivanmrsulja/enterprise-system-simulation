package rs.enterprise.paymentserviceprovider.unit;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import rs.enterprise.paymentserviceprovider.exception.NotFoundException;
import rs.enterprise.paymentserviceprovider.model.Authority;
import rs.enterprise.paymentserviceprovider.model.Merchant;
import rs.enterprise.paymentserviceprovider.model.TwoFactorAuthenticationToken;
import rs.enterprise.paymentserviceprovider.repository.MerchantRepository;
import rs.enterprise.paymentserviceprovider.repository.TwoFactorAuthenticationTokenRepository;
import rs.enterprise.paymentserviceprovider.service.TwoFactorAuthenticationService;
import rs.enterprise.paymentserviceprovider.util.EncryptionUtil;
import rs.enterprise.paymentserviceprovider.util.SecureStringGenerator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TwoFactorAutheticatioServiceTest {

    @Mock
    private JavaMailSender emailSender;
    @Mock
    private TwoFactorAuthenticationTokenRepository twoFactorAuthenticationTokenRepository;
    @Mock
    private MerchantRepository merchantRepository;
    @Mock
    private SecureStringGenerator secureStringGenerator;
    @Mock
    private EncryptionUtil encryptionUtil;

    @InjectMocks
    private TwoFactorAuthenticationService service;

    @Test
    void createNewAuthToken_calledWithValidParams_isSuccess() throws Exception {
        // GIVEN
        var authority = new Authority("ROLE_MERCHANT");
        var merchant = new Merchant("id",
                "password",
                "key",
                "name",
                "email",
                authority);
        merchant.setId(1);
        doReturn(Optional.of(merchant)).when(merchantRepository).findByMerchantId(merchant.getMerchantId());
        doReturn("aaaaa").when(secureStringGenerator).generate(5);
        doReturn("token").when(encryptionUtil).encrypt("token");

        // WHEN
        service.createNewAuthToken(merchant.getMerchantId(), "token");

        // THEN
        verify(twoFactorAuthenticationTokenRepository, times(1)).save(any());
    }

    @Test
    void createNewAuthToken_calledWithInvalidParams_throwsException() throws Exception {
        // GIVEN
        var authority = new Authority("ROLE_MERCHANT");
        var merchant = new Merchant("id",
                "password",
                "key",
                "name",
                "email",
                authority);
        doThrow(NotFoundException.class).when(merchantRepository).findByMerchantId(merchant.getMerchantId());

        // THEN
        assertThrows(NotFoundException.class, () -> service.createNewAuthToken(merchant.getMerchantId(), "aaaaa"));
    }

    @Test
    void verifyToken_calledWithValidParams_isSuccess() throws Exception {
        // GIVEN
        var authority = new Authority("ROLE_MERCHANT");
        var merchant = new Merchant("id",
                "password",
                "key",
                "name",
                "email",
                authority);

        var token = new TwoFactorAuthenticationToken();
        doReturn(Optional.of(token)).when(twoFactorAuthenticationTokenRepository).verifyToken(merchant.getMerchantId(), "12345");
        doReturn("token").when(encryptionUtil).decrypt(any());

        // WHEN
        service.verifyToken(merchant.getMerchantId(), "12345");

        // THEN
        verify(twoFactorAuthenticationTokenRepository, times(1)).delete(token);
    }

    @Test
    void verifyToken_calledWithInvalidParams_isSuccess() throws Exception {
        // GIVEN
        var authority = new Authority("ROLE_MERCHANT");
        var merchant = new Merchant("id",
                "password",
                "key",
                "name",
                "email",
                authority);
        doThrow(NotFoundException.class).when(twoFactorAuthenticationTokenRepository).verifyToken(merchant.getMerchantId(), "12345");

        // THEN
        assertThrows(NotFoundException.class, () -> service.verifyToken(merchant.getMerchantId(), "12345"));
    }
}
