package rs.enterprise.paymentserviceprovider.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class BitcoinHashService {

    private String transactionHash = "";

}
