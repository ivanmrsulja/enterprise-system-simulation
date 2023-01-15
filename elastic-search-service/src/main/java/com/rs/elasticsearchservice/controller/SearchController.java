package com.rs.elasticsearchservice.controller;

import com.rs.elasticsearchservice.dto.CandidateApplicationDTO;
import com.rs.elasticsearchservice.model.AdvancedQuery;
import com.rs.elasticsearchservice.service.ResultRetrieverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final ResultRetrieverService resultRetrieverService;


    @Autowired
    public SearchController(ResultRetrieverService resultRetrieverService) {
        this.resultRetrieverService = resultRetrieverService;
    }

    @PostMapping(value="/boolean", consumes="application/json")
    public ResponseEntity<List<CandidateApplicationDTO>> searchBoolean(HttpServletRequest request, @RequestBody AdvancedQuery advancedQuery){
        var results = resultRetrieverService.getResults(advancedQuery);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}
