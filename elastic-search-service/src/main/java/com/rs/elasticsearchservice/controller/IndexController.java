package com.rs.elasticsearchservice.controller;

import com.rs.elasticsearchservice.annotation.CheckApiKey;
import com.rs.elasticsearchservice.dto.CandidateApplicationIndexDTO;
import com.rs.elasticsearchservice.dto.StatisticLogDTO;
import com.rs.elasticsearchservice.service.IndexingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/index")
public class IndexController {

    private final IndexingService indexingService;

    private static final Logger LOG = Logger.getLogger(SearchController.class.getName());

    @Autowired
    public IndexController(IndexingService indexingService) {
        this.indexingService = indexingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void index(CandidateApplicationIndexDTO indexingUnit) throws IOException {
        indexingService.indexApplication(indexingUnit);
    }

    @PostMapping("/statistic-log")
    @ResponseStatus(HttpStatus.CREATED)
    @CheckApiKey
    public void createStatisticLog(@RequestBody StatisticLogDTO statisticLog, @RequestHeader("X-API-KEY") String acquiredApiKey) {
        LOG.info("STATISTIC-LOG " + statisticLog.getCity() + "-" + statisticLog.getUser() + "-" + statisticLog.getCompany());
    }
}
