package rs.enterprise.paymentserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcquirerBankPaymentRequestDTO {

    @NotBlank(message = "Merchant ID is mandatory")
    private String merchantId;

    @NotBlank(message = "Merchant password is mandatory")
    private String merchantPassword;

    @PositiveOrZero(message = "Amount must be a positive number")
    private Double amount;

    private Long merchantOrderId;

    @NotBlank(message = "Merchant timestamp is mandatory")
    private String merchantTimestamp;

    @NotBlank(message = "Success URL is mandatory")
    private String successUrl;

    @NotBlank(message = "Failed URL is mandatory")
    private String failedUrl;

    @NotBlank(message = "Error URL is mandatory")
    private String errorUrl;
}
