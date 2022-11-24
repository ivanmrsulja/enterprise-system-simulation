package rs.enterprise.paymentserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcquirerBankPaymentRequestDTO {

    private String merchantId;

    private String merchantPassword;

    private Double amount;

    private Long merchantOrderId;

    private String merchantTimestamp;

    private String successUrl;

    private String failedUrl;

    private String errorUrl;
}
