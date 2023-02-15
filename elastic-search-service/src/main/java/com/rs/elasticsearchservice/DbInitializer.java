package com.rs.elasticsearchservice;

import com.rs.elasticsearchservice.client.ElasticsearchIndexClient;
import com.rs.elasticsearchservice.client.LocationIqClient;
import com.rs.elasticsearchservice.controller.SearchController;
import com.rs.elasticsearchservice.databaserepository.AuthorityRepository;
import com.rs.elasticsearchservice.databaserepository.EmployeeRepository;
import com.rs.elasticsearchservice.model.Authority;
import com.rs.elasticsearchservice.model.CandidateApplication;
import com.rs.elasticsearchservice.model.Employee;
import com.rs.elasticsearchservice.repository.CandidateApplicationRepository;
import com.rs.elasticsearchservice.util.TestingTextSnippets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Slf4j
@Component
@RequiredArgsConstructor
public class DbInitializer implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;

    private final CandidateApplicationRepository candidateApplicationRepository;

    private final LocationIqClient locationIqClient;

    private final ElasticsearchIndexClient indexClient;

    private final AuthorityRepository authorityRepository;

    private final EmployeeRepository employeeRepository;

    private static final Logger LOG = Logger.getLogger(SearchController.class.getName());

    @Value("${location.api.key}")
    private String apiKey;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        configureAnalyzer();

        populateApplicationIndex();

        populateUsers();
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
        CandidateApplication candidate1 = new CandidateApplication("Ivan", "Mrsulja", "BSC", TestingTextSnippets.cv1, TestingTextSnippets.letter1, "Maksima Gorkog 17a, Novi Sad", new GeoPoint(addr1.getLat(),addr1.getLon()));
        candidate1.setCvPath("cv1.pdf");
        candidate1.setLetterPath("letter1.pdf");
        candidateApplicationRepository.save(candidate1);
        CandidateApplication candidate2 = new CandidateApplication("Milos", "Popovic", "PHD", TestingTextSnippets.cv2, TestingTextSnippets.letter2, "Bulevar Oslobodjenja 16, Novi Sad", new GeoPoint(addr2.getLat(),addr2.getLon()));
        candidate2.setCvPath("cv2.pdf");
        candidate2.setLetterPath("letter2.pdf");
        candidateApplicationRepository.save(candidate2);
        CandidateApplication candidate3 = new CandidateApplication("Veljko", "Tosic", "MSC", TestingTextSnippets.cv3, TestingTextSnippets.letter3, "Vidikovacki Venac 27, Beograd, Rakovica", new GeoPoint(addr3.getLat(),addr3.getLon()));
        candidate3.setCvPath("cv3.pdf");
        candidate3.setLetterPath("letter3.pdf");
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

    private void populateUsers() {
        var worker = new Authority("WORKER");
        var boss = new Authority("BOSS");
        var indexWorker = new Authority("INDEX_WORKER");
        authorityRepository.save(worker);
        authorityRepository.save(boss);
        authorityRepository.save(indexWorker);

        var worker1 = new Employee("CandidateWorker1", passwordEncoder.encode("bpm"), worker);
        var boss1 = new Employee("CandidateBoss1", passwordEncoder.encode("bpm"), boss);
        var companyWorker1 = new Employee("CandidateIndexer1", passwordEncoder.encode("bpm"), indexWorker);
        employeeRepository.save(worker1);
        employeeRepository.save(boss1);
        employeeRepository.save(companyWorker1);
    }
}
