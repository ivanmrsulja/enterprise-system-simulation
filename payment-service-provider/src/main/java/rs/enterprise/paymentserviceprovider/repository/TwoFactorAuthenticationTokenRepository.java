package rs.enterprise.paymentserviceprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.enterprise.paymentserviceprovider.model.TwoFactorAuthenticationToken;

import java.util.Optional;

@Repository
public interface TwoFactorAuthenticationTokenRepository extends JpaRepository<TwoFactorAuthenticationToken, Integer> {

    @Query("select t from TwoFactorAuthenticationToken t where t.merchantId = :merchantId and t.pinCode = :pinCode")
    Optional<TwoFactorAuthenticationToken> verifyToken(String merchantId, String pinCode);
}
