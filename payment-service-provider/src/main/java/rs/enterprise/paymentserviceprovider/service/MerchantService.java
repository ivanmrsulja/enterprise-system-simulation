package rs.enterprise.paymentserviceprovider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.enterprise.paymentserviceprovider.dto.RegisterDTO;
import rs.enterprise.paymentserviceprovider.exception.NotFoundException;
import rs.enterprise.paymentserviceprovider.model.Merchant;
import rs.enterprise.paymentserviceprovider.repository.AuthorityRepository;
import rs.enterprise.paymentserviceprovider.repository.MerchantRepository;
import rs.enterprise.paymentserviceprovider.util.SecureStringGenerator;

import javax.transaction.Transactional;
import java.security.SecureRandom;

@Service
@Transactional
public class MerchantService implements UserDetailsService {

    private final MerchantRepository merchantRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecureStringGenerator secureStringGenerator;

    @Autowired
    public MerchantService(MerchantRepository merchantRepository,
                           AuthorityRepository authorityRepository,
                           PasswordEncoder passwordEncoder,
                           SecureStringGenerator secureStringGenerator) {
        this.merchantRepository = merchantRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.secureStringGenerator = secureStringGenerator;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return merchantRepository.findByMerchantId(username).orElseThrow(() -> new NotFoundException(String.format("Merchant with ID: %s not found.", username)));
    }

    public Merchant register(RegisterDTO registrationRequest) {
        var apiKey = secureStringGenerator.generate(128);

        var authority = authorityRepository.findByName("ROLE_MERCHANT").orElseThrow(() -> new NotFoundException("Authority with given name does not exist."));
        var user = new Merchant(registrationRequest.getMerchantId(), passwordEncoder.encode(registrationRequest.getMerchantPassword()), apiKey, registrationRequest.getName(), registrationRequest.getEmail(), authority);
        return merchantRepository.save(user);
    }

    public Merchant findById(Integer id) {
        return merchantRepository.findById(id).orElseThrow(() -> new NotFoundException("Merchant with given ID does not exist."));
    }
}
