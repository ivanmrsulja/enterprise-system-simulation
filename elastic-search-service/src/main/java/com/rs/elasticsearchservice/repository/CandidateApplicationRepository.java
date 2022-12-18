package com.rs.elasticsearchservice.repository;

import com.rs.elasticsearchservice.model.CandidateApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateApplicationRepository extends ElasticsearchRepository<CandidateApplication, String> {

    @Query("{\"bool\" : " +
            "{\"should\" : [ " +
            "{\"field\" : {\"name\" : \"?\"}}, " +
            "{\"field\" : {\"surname\" : \"?\"}}, " +
            "{\"field\" : {\"education\" : \"?\"}} " +
            "{\"field\" : {\"cv\" : \"?\"}} " +
            "{\"field\" : {\"letter\" : \"?\"}} " +
            "]}}")
    Page<CandidateApplication> findByNameOrSurnameOrEducation(String name, String surname, String education, String cv, String letter, Pageable pageable);
}
