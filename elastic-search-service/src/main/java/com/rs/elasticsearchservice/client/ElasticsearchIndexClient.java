package com.rs.elasticsearchservice.client;

import com.rs.elasticsearchservice.dto.AcknowledgementResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "elasticsearchIndexClient", url = "http://localhost:9200")
public interface ElasticsearchIndexClient {

    @PostMapping("/applications/_close")
    AcknowledgementResponseDTO closeIndex();

    @PostMapping("/applications/_open")
    AcknowledgementResponseDTO openIndex();

    @PutMapping(value = "/applications/_settings", consumes = "application/json")
    AcknowledgementResponseDTO updateSettings(@RequestBody String configurationJSON);

    @DeleteMapping(value = "/statistic-data")
    AcknowledgementResponseDTO dropStatisticData();
}
