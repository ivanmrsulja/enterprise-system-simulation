package rs.enterprise.paymentserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {

    @NotBlank(message = "Merchant ID is mandatory")
    private String merchantId;

    @NotBlank(message = "Merchant password is mandatory")
    private String merchantPassword;

    @NotBlank(message = "Company name is mandatory")
    private String name;

    @Email(message = "Valid email is mandatory")
    private String email;

}
