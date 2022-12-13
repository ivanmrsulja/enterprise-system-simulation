package com.rs.elasticsearchservice.controller;

import com.rs.elasticsearchservice.dto.CandidateApplicationDTO;
import com.rs.elasticsearchservice.model.AdvancedQuery;
import com.rs.elasticsearchservice.model.RequiredHighlights;
import com.rs.elasticsearchservice.service.ResultRetriever;
import com.rs.elasticsearchservice.util.ExpressionTransformer;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final ResultRetriever resultRetriever;

    private final ExpressionTransformer expressionTransformer;

    @Autowired
    public SearchController(ResultRetriever resultRetriever,
                            ExpressionTransformer expressionTransformer) {
        this.resultRetriever = resultRetriever;
        this.expressionTransformer = expressionTransformer;
    }

    @PostMapping(value="/boolean", consumes="application/json")
    public ResponseEntity<List<CandidateApplicationDTO>> searchBoolean(@RequestBody AdvancedQuery advancedQuery){
        var requiredHighlights = new ArrayList<RequiredHighlights>();

        var postfixExpression = expressionTransformer.transformToPostFixNotation(advancedQuery.getExpression());
//        System.out.println(postfixExpression);
        var finalQuery = expressionTransformer.buildQueryFromPostFixExpression(postfixExpression, requiredHighlights);

        finalQuery = QueryBuilders.boolQuery()
                .must(finalQuery)
                .must(QueryBuilders.geoDistanceQuery("location")
                        .distance(100, DistanceUnit.KILOMETERS)
                        .point(45.25,19.81));

        List<CandidateApplicationDTO> results = resultRetriever.getResults(finalQuery, requiredHighlights);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}
