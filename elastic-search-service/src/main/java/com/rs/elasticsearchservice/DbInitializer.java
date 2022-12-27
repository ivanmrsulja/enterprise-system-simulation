package com.rs.elasticsearchservice;

import com.rs.elasticsearchservice.client.LocationIqClient;
import com.rs.elasticsearchservice.model.CandidateApplication;
import com.rs.elasticsearchservice.repository.CandidateApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    private final LocationIqClient locationIqClient;

    @Value("${location.api.key}")
    private String apiKey;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        var addr1 = locationIqClient.forwardGeolocation(apiKey, "Maksima Gorkog 17a, Novi Sad", "json").get(0);
        Thread.sleep(2000);
        var addr2 = locationIqClient.forwardGeolocation(apiKey, "Bulevar Oslobodjenja 16, Novi Sad", "json").get(0);
        Thread.sleep(2000);
        var addr3 = locationIqClient.forwardGeolocation(apiKey, "Vidikovacki Venac 27, Beograd, Rakovica", "json").get(0);

        candidateApplicationRepository.deleteAll();
        CandidateApplication candidate1 = new CandidateApplication("Ivan", "Mrsulja", "BSC", "aaaaa dolor sit amet", "bbbbb", new GeoPoint(addr1.getLat(),addr1.getLon()));
        candidateApplicationRepository.save(candidate1);
        CandidateApplication candidate2 = new CandidateApplication("Milos", "Popovic", "PHD", "Lorem ipsum aaaaa", "bbbbb", new GeoPoint(addr2.getLat(),addr2.getLon()));
        candidateApplicationRepository.save(candidate2);
        CandidateApplication candidate3 = new CandidateApplication("Veljko", "Tosic", "MSC", "aaaaa", "bbbbb", new GeoPoint(addr3.getLat(),addr3.getLon()));
        candidateApplicationRepository.save(candidate3);

        Page<CandidateApplication> candidatesByName
                = candidateApplicationRepository.findByNameOrSurnameOrEducation("Ivan", "Mrsulja", "MSC", "", "",PageRequest.of(0, 10));
//        System.out.println(candidatesByName.getTotalElements());
    }
}