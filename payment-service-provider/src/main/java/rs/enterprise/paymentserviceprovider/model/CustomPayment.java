package rs.enterprise.paymentserviceprovider.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomPayment {

    private Double amount;

    private LocalDate date;

    // Paypal
    private String currency;

    private String description;

    private String cancelUrl;

    private String successUrl;

    @Value("${paypal.business-email}")
    private String toBusinessCompanyEmail;

    // Bitcoin
    @Value("${bitcoin.business-wallet}")
    private String toBusinessCompanyWallet;

}
