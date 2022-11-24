package rs.enterprise.paymentserviceprovider.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "authorities")
public class Authority extends BaseEntity implements GrantedAuthority {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "authority_privileges", joinColumns = @JoinColumn(name = "authority_id"), inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    private Set<Privilege> privileges = new HashSet<>();

    public Authority() {
    }

    public Authority(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }

    public Authority addPrivilege(Privilege privilege) {
        this.privileges.add(privilege);
        return this;
    }
}
