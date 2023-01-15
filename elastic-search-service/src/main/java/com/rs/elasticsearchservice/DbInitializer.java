package com.rs.elasticsearchservice;

import com.rs.elasticsearchservice.client.ElasticsearchIndexClient;
import com.rs.elasticsearchservice.client.LocationIqClient;
import com.rs.elasticsearchservice.controller.SearchController;
import com.rs.elasticsearchservice.model.CandidateApplication;
import com.rs.elasticsearchservice.repository.CandidateApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Slf4j
@Component
@RequiredArgsConstructor
public class DbInitializer implements ApplicationRunner {

    private final CandidateApplicationRepository candidateApplicationRepository;

    private final LocationIqClient locationIqClient;

    private final ElasticsearchIndexClient indexClient;

    private static final Logger LOG = Logger.getLogger(SearchController.class.getName());

    @Value("${location.api.key}")
    private String apiKey;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        configureAnalyzer();

        populateApplicationIndex();
    }

    private void configureAnalyzer() throws IOException {
        indexClient.closeIndex();

        var result = indexClient.updateSettings(
                new String(
                        Files.readAllBytes(
                                Paths.get("src/main/resources/configuration/serbian-analyzer-config.json")
                        )
                ));

        indexClient.openIndex();

        indexClient.dropStatisticData();
        populateStatisticLogs();

        if(result.getAcknowledged()) {
            log.info("Serbian analyzer configured successfully.");
        }
    }

    private void populateApplicationIndex() throws InterruptedException {
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

//        Page<CandidateApplication> candidatesByName
//                = candidateApplicationRepository.findByNameOrSurnameOrEducation("Ivan", "Mrsulja", "MSC", "", "",PageRequest.of(0, 10));
//        System.out.println(candidatesByName.getTotalElements());
    }

    private void populateStatisticLogs() {
        LOG.info("STATISTIC-LOG Novi Sad-Ivan Mrsulja (ivanmrsulja)-CyberLab Solutions");
        LOG.info("STATISTIC-LOG Novi Sad-Dimitrije Karanfilovic (dryzen99)-CyberLab Solutions");
        LOG.info("STATISTIC-LOG Beograd-Dusan Erdeljan (erdeljandusan)-TensTorrent");
        LOG.info("STATISTIC-LOG Beograd-Maja Jankovic (majaja)-TensTorrent");
        LOG.info("STATISTIC-LOG Beograd-Nikola Nikolic (nikola89)-DECenter");
        LOG.info("STATISTIC-LOG Nis-Marko Markovic (markomarkovic99)-Diligent");
        LOG.info("STATISTIC-LOG Trstenik-Ivan Mrsulja (ivanmrsulja)-Prva Petoletka Namenska");
        LOG.info("STATISTIC-LOG Zrenjanin-Jovan Joksovic (jovo98)-Vega IT");
    }
}
