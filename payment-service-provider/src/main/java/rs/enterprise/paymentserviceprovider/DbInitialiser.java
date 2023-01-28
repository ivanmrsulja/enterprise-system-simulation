package rs.enterprise.paymentserviceprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.enterprise.paymentserviceprovider.model.Authority;
import rs.enterprise.paymentserviceprovider.model.BusinessAccount;
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
                            "mqelI4wskvNhTX0GVyz3W0PUxVf8xBSXREtP3pG9xhyznZ4lht7CYMsdVrmSWkoGbv6uYBTEANsHCxmPqslIUh5TD2vSLkKPOB37HA3Nk9LblMJzaAqpfFxKhGQgRrQ2",
                            "Monsters INC", "mrsuljaivancic@gmail.com", authority1);
        user1.setPaymentMethods(new HashSet<>(List.of(new String[]{"Credit Card", "QR Code", "paypal", "bitcoin"})));
        user1.setAccounts(new HashSet<>(List.of(new BusinessAccount("paypal",
                "sb-43hmsz22738278@business.example.com"), new BusinessAccount("bitcoin",
                "mohjSavDdQYHRYXcS3uS6ttaHP8amyvX78"))));
        merchantRepository.save(user1);
    }
}
