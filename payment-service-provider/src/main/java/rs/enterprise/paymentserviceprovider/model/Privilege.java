package rs.enterprise.paymentserviceprovider.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "privileges")
@NoArgsConstructor
public class Privilege extends BaseEntity implements GrantedAuthority {

    @Column(name = "name")
    private String name;

    public Privilege(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}
