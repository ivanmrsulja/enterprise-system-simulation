package com.rs.elasticsearchservice;

import com.rs.elasticsearchservice.model.CandidateApplication;
import com.rs.elasticsearchservice.repository.CandidateApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DbInitializer implements ApplicationRunner {

    private final CandidateApplicationRepository candidateApplicationRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        candidateApplicationRepository.deleteAll();
        CandidateApplication candidate1 = new CandidateApplication("Ivan", "Mrsulja", "BSC", "aaaaa dolor sit amet", "bbbbb", new GeoPoint(45.25,19.81));
        candidateApplicationRepository.save(candidate1);
        CandidateApplication candidate2 = new CandidateApplication("Milos", "Popovic", "PHD", "Lorem ipsum aaaaa", "bbbbb", new GeoPoint(45.25,19.81));
        candidateApplicationRepository.save(candidate2);
//        CandidateApplication candidate3 = new CandidateApplication("Veljko", "Tosic", "MSC", "aaaaa", "bbbbb", new GeoPoint(45.25,19.81));
        CandidateApplication candidate3 = new CandidateApplication("Veljko", "Tosic", "MSC", "aaaaa", "bbbbb", new GeoPoint(46.04, 14.51));
        candidateApplicationRepository.save(candidate3);

        Page<CandidateApplication> candidatesByName
                = candidateApplicationRepository.findByNameOrSurnameOrEducation("Ivan", "Mrsulja", "MSC", PageRequest.of(0, 10));
//        System.out.println(candidatesByName.getTotalElements());
    }
}
