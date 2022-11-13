package rs.enterprise.paymentserviceprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.enterprise.paymentserviceprovider.model.Merchant;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Integer> {

    @Query("select m from Merchant m where m.merchantId = :merchantId")
    Optional<Merchant> findByMerchantId(String merchantId);
}
