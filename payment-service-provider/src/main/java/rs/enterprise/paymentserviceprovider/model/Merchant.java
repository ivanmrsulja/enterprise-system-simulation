package rs.enterprise.paymentserviceprovider.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "merchants")
@NoArgsConstructor
public class Merchant implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "merchant_payment_methods", joinColumns = @JoinColumn(name = "merchant_id"))
    private Set<String> paymentMethods = new HashSet<>();

    @Column(name = "merchant_id", unique = true)
    private String merchantId;

    @Column(name = "merchant_password")
    private String merchantPassword;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "authority_id")
    private Authority authority;

    public Merchant(String merchantId, String merchantPassword, String apiKey, String name, Authority authority) {
        this.merchantId = merchantId;
        this.merchantPassword = merchantPassword;
        this.apiKey = apiKey;
        this.name = name;
        this.authority = authority;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authority.getPrivileges();
    }

    @Override
    public String getPassword() {
        return getMerchantPassword();
    }

    @Override
    public String getUsername() {
        return getMerchantId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
