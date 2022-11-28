package rs.enterprise.paymentserviceprovider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.enterprise.paymentserviceprovider.model.BankPayment;

import java.util.Optional;

@Repository
public interface BankPaymentRepository extends JpaRepository<BankPayment, Integer> {

    @Query("select bp from BankPayment bp where bp.id = :id and bp.merchantOrderId = :merchantOrderId")
    Optional<BankPayment> getByIdAndMerchantOrderId(Integer id, Long merchantOrderId);

    @Query("select bp from BankPayment bp where bp.merchantOrderId = :merchantOrderId")
    Optional<BankPayment> getByMerchantOrderId(Long merchantOrderId);
}
