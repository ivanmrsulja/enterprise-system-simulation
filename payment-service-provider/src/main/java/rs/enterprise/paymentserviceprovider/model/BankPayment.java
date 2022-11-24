package rs.enterprise.paymentserviceprovider.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bank_payments")
public class BankPayment extends BaseEntity {

    @Column(name = "merchant_id")
    private String MerchantId;

    @Column(name = "merchant_password")
    private String MerchantPassword;

    @Column(name = "merchant_order_id")
    private Long merchantOrderId;

    @Column(name = "merchant_timestamp")
    private String merchantTimestamp;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "successUrl")
    private String successUrl;

    @Column(name = "failed_url")
    private String failedUrl;

    @Column(name = "error_url")
    private String errorUrl;
}
