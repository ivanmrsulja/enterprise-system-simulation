package com.rs.elasticsearchservice.controller;

import com.rs.elasticsearchservice.dto.CandidateApplicationIndexDTO;
import com.rs.elasticsearchservice.util.PDFHandler;
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

    private final PDFHandler pdfHandler;

    @Autowired
    public IndexController(PDFHandler pdfHandler) {
        this.pdfHandler = pdfHandler;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void index(CandidateApplicationIndexDTO indexingUnit) throws IOException {
        var cvPath = pdfHandler.saveUploadedFile(indexingUnit.getCv());
        var letterPath = pdfHandler.saveUploadedFile(indexingUnit.getMotivationLetter());

        var application = pdfHandler.getWithIndexedDocuments(cvPath, letterPath);
        application.setName(indexingUnit.getName());
        application.setSurname(indexingUnit.getSurname());
        application.setEducation(indexingUnit.getEducation());

        // TODO: index geolocation
        // TODO: save to elasticsearch repo
    }
}
