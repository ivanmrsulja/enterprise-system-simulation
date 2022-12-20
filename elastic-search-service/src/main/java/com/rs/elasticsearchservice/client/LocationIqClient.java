package com.rs.elasticsearchservice.client;

import com.rs.elasticsearchservice.dto.ForwardGeolocationResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "locationClient", url = "https://us1.locationiq.com/v1")
public interface LocationIqClient {

    @GetMapping("/search")
    List<ForwardGeolocationResponseDTO> forwardGeolocation(@RequestParam String key, @RequestParam String q, @RequestParam String format);
}
