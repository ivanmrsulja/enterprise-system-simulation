package com.rs.elasticsearchservice.databaserepository;

import com.rs.elasticsearchservice.model.LoginToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginTokenRepository extends JpaRepository<LoginToken, Integer> {

    @Query("select lt from LoginToken lt where lt.fetchToken = :fetchToken")
    Optional<LoginToken> fetchToken(String fetchToken);
}
