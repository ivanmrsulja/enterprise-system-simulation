package rs.enterprise.paymentserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.enterprise.paymentserviceprovider.model.enums.TransactionState;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcquirerBankFinalStepDTO {
    private Long merchantOrderId;
    private Long paymentId;
    private Long acquirerOrderId;
    private String acquirerTimestamp;
    private TransactionState transactionState;
}
