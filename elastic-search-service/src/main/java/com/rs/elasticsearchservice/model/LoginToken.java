package com.rs.elasticsearchservice.model;

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
@Table(name = "login_tokens")
public class LoginToken extends BaseEntity {

    @Column(name = "real_token", length = 1024)
    private String realToken;

    @Column(name = "fetch_token")
    private String fetchToken;
}
