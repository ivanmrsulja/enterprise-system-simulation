package rs.enterprise.paymentserviceprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.enterprise.paymentserviceprovider.model.Authority;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    @Query("select a from Authority a where a.name = :name")
    Optional<Authority> findByName(String name);
}
