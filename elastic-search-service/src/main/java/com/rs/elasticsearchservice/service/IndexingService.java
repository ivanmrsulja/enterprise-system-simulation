package com.rs.elasticsearchservice.service;

import com.rs.elasticsearchservice.client.LocationIqClient;
import com.rs.elasticsearchservice.dto.CandidateApplicationIndexDTO;
import com.rs.elasticsearchservice.repository.CandidateApplicationRepository;
import com.rs.elasticsearchservice.util.PDFHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class IndexingService {

    private final PDFHandler pdfHandler;

    private final LocationIqClient locationIqClient;

    private final CandidateApplicationRepository repository;

    @Value("${location.api.key}")
    private String apiKey;

    @Autowired
    public IndexingService(PDFHandler pdfHandler,
                           LocationIqClient locationIqClient,
                           CandidateApplicationRepository repository) {
        this.pdfHandler = pdfHandler;
        this.locationIqClient = locationIqClient;
        this.repository = repository;
    }

    public void indexApplication(CandidateApplicationIndexDTO indexingUnit) throws IOException {
        var cvPath = pdfHandler.saveUploadedFile(indexingUnit.getCv());
        var letterPath = pdfHandler.saveUploadedFile(indexingUnit.getMotivationLetter());

        var application = pdfHandler.getWithIndexedDocuments(cvPath, letterPath);
        application.setName(indexingUnit.getName());
        application.setSurname(indexingUnit.getSurname());
        application.setEducation(indexingUnit.getEducation());

        var location = locationIqClient.
                forwardGeolocation(apiKey, indexingUnit.getAddress(), "json").get(0);

        application.setLocation(new GeoPoint(location.getLat(), location.getLon()));

        repository.save(application);
    }
}
