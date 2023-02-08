package com.rs.elasticsearchservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authorities")
public class Authority extends BaseEntity implements GrantedAuthority {

    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
