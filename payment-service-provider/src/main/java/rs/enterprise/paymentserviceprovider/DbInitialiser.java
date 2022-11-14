package rs.enterprise.paymentserviceprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.enterprise.paymentserviceprovider.model.Authority;
import rs.enterprise.paymentserviceprovider.model.Merchant;
import rs.enterprise.paymentserviceprovider.repository.AuthorityRepository;
import rs.enterprise.paymentserviceprovider.repository.MerchantRepository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;

@Component
public class DbInitialiser implements ApplicationRunner {

    private final MerchantRepository merchantRepository;

    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DbInitialiser(MerchantRepository merchantRepository,
                         AuthorityRepository authorityRepository,
                         PasswordEncoder passwordEncoder) {
        this.merchantRepository = merchantRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        var authority1 = new Authority("ROLE_MERCHANT");
        authorityRepository.save(authority1);

        var user1 = new Merchant("j4komDDBwXVWoHg4ej6WMVqdD6U9qE",
                            passwordEncoder.encode("j0n1KCQZmvFKogkY5OyE30hHfe9DWSxi2JQ4A47PoBnMWSJ6jUfpCHNywtJVIJYNdFYD3kK8ZE6NlZuQNXPbcZriPdIgxuW6ijrg"),
                            "asdasd", "Ime", authority1);
        user1.setPaymentMethods(new HashSet<>(List.of(new String[]{"Credit Card", "PayPal", "Bitcoin"})));
        merchantRepository.save(user1);
    }
}
