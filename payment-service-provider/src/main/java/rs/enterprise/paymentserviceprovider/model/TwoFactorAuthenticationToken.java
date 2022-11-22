package rs.enterprise.paymentserviceprovider.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "two_factor_auth_tokens")
public class TwoFactorAuthenticationToken extends BaseEntity {

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "pin_code")
    private String pinCode;

    @Column(name = "token", length = 1024)
    private String token;
}
