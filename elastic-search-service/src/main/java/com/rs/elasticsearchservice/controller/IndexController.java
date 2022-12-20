package com.rs.elasticsearchservice.controller;

import com.rs.elasticsearchservice.dto.CandidateApplicationIndexDTO;
import com.rs.elasticsearchservice.service.IndexingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/index")
public class IndexController {

    private final IndexingService indexingService;

    @Autowired
    public IndexController(IndexingService indexingService) {
        this.indexingService = indexingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void index(CandidateApplicationIndexDTO indexingUnit) throws IOException {
        indexingService.indexApplication(indexingUnit);
    }
}
