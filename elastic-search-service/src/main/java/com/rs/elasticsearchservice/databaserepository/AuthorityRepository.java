package com.rs.elasticsearchservice.databaserepository;

import com.rs.elasticsearchservice.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
}
