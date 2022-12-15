package rs.enterprise.paymentserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequestDTO {

    @NotBlank(message = "Merchant ID is mandatory")
    private String merchantId;

    @NotBlank(message = "Merchant password is mandatory")
    private String merchantPassword;
}
