package com.rs.elasticsearchservice.client;

import com.rs.elasticsearchservice.dto.AcknowledgementResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "elasticsearchIndexClient", url = "http://localhost:9200/applications")
public interface ElasticsearchIndexClient {

    @PostMapping("/_close")
    AcknowledgementResponseDTO closeIndex();

    @PostMapping("/_open")
    AcknowledgementResponseDTO openIndex();

    @PutMapping(value = "/_settings", consumes = "application/json")
    AcknowledgementResponseDTO updateSettings(@RequestBody String configurationJSON);
}
