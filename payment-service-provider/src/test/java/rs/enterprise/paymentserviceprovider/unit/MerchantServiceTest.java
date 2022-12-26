package rs.enterprise.paymentserviceprovider.unit;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import rs.enterprise.paymentserviceprovider.dto.RegisterDTO;
import rs.enterprise.paymentserviceprovider.exception.NotFoundException;
import rs.enterprise.paymentserviceprovider.model.Authority;
import rs.enterprise.paymentserviceprovider.model.Merchant;
import rs.enterprise.paymentserviceprovider.repository.AuthorityRepository;
import rs.enterprise.paymentserviceprovider.repository.MerchantRepository;
import rs.enterprise.paymentserviceprovider.service.MerchantService;
import rs.enterprise.paymentserviceprovider.util.SecureStringGenerator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MerchantServiceTest {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecureStringGenerator secureStringGenerator;

    @InjectMocks
    private MerchantService merchantService;

    @Test
    void register_whenCalledWithValidData_isSuccess() {
        // GIVEN
        var authority = new Authority("ROLE_MERCHANT");
        var registerRequest = new RegisterDTO("aaaaa",
                "password",
                "name",
                "email");
        var merchant = new Merchant(registerRequest.getMerchantId(),
                "password",
                "key",
                registerRequest.getName(),
                registerRequest.getEmail(),
                authority);
        doReturn(Optional.of(authority)).when(authorityRepository).findByName(authority.getName());
        doReturn("key").when(secureStringGenerator).generate(128);
        doReturn("password").when(passwordEncoder).encode(registerRequest.getMerchantPassword());
        doReturn(merchant).when(merchantRepository).save(any());

        // WHEN
        var newMerchant = merchantService.register(registerRequest);

        // THEN
        verify(secureStringGenerator, times(1)).generate(128);
        verify(passwordEncoder, times(1)).encode("password");
        assertEquals("key", newMerchant.getApiKey());
        assertEquals("password", newMerchant.getPassword());
    }

    @Test
    void register_whenCalledWithNonExistingAuthority_throwsException() {
        // GIVEN
        var authority = new Authority("ROLE_MERCHANT");
        var registerRequest = new RegisterDTO("aaaaa",
                "password",
                "name",
                "email");
        doThrow(NotFoundException.class).when(authorityRepository).findByName(authority.getName());


        // THEN
        assertThrows(NotFoundException.class, () -> merchantService.register(registerRequest));
    }
}
