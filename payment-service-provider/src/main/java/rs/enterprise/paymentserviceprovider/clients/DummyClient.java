package rs.enterprise.paymentserviceprovider.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import rs.enterprise.paymentserviceprovider.config.CustomFeignConfig;
import rs.enterprise.paymentserviceprovider.dto.DummyDTO;

@FeignClient(value = "dummyClient", url = "https://www.sep-banka.com:8082/api/dummy/all", configuration = CustomFeignConfig.class)
public interface DummyClient {

    @GetMapping
    DummyDTO getData();
}
