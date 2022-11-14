package rs.enterprise.paymentserviceprovider.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import rs.enterprise.paymentserviceprovider.config.CustomFeignConfig;
import rs.enterprise.paymentserviceprovider.dto.AcquirerBankMerchantAuthenticationDTO;
import rs.enterprise.paymentserviceprovider.dto.AcquirerBankPaymentRequestDTO;
import rs.enterprise.paymentserviceprovider.dto.BankRedirectResponseDTO;

@FeignClient(value = "acquirerBankClient", url = "https://localhost:8082/api/acquirer-bank", configuration = CustomFeignConfig.class)
public interface AcquirerBankClient {

    @PostMapping(value = "/authenticate")
    boolean authenticateMerchant(@RequestHeader("X-Auth-Token") String apiKey, @RequestBody AcquirerBankMerchantAuthenticationDTO authRequest);

    @PostMapping(value = "/request-redirect")
    BankRedirectResponseDTO requestRedirect(@RequestHeader("X-API-Key") String apiKey, @RequestBody AcquirerBankPaymentRequestDTO paymentRequest);
}
