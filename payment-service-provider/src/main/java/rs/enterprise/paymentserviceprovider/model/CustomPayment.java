package rs.enterprise.paymentserviceprovider.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomPayment {

    private Double amount;

    private LocalDate date;

    private String paymentMethod;

    private Long merchantOrderId;

    private Integer transactionId;

    private String currency;

    private String description;

    private String cancelUrl;

    private String successUrl;

    private String account;

    private Integer numberOfMonths;

    @Override
    public String toString() {
        return "CustomPayment{" +
                "amount=" + amount +
                ", date=" + date +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", merchantOrderId=" + merchantOrderId +
                ", transactionId=" + transactionId +
                ", currency='" + currency + '\'' +
                ", description='" + description + '\'' +
                ", cancelUrl='" + cancelUrl + '\'' +
                ", successUrl='" + successUrl + '\'' +
                ", account='" + account + '\'' +
                ", numberOfMonths=" + numberOfMonths +
                '}';
    }
}
