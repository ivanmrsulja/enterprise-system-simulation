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

    private String MerchantId;

    private String MerchantPassword;

    private Double Amount;

    private Long MerchantOrderId;

    private String MerchantTimestamp;

    private String SuccessUrl;

    private String FailedUrl;

    private String ErrorUrl;
}
