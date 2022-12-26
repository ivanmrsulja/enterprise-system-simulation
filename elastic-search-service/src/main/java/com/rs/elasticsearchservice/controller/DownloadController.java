package com.rs.elasticsearchservice.controller;

import com.rs.elasticsearchservice.util.PDFHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("api/download")
public class DownloadController {

    private final PDFHandler pdfHandler;

    @Autowired
    public DownloadController(PDFHandler pdfHandler) {
        this.pdfHandler = pdfHandler;
    }

    @GetMapping
    public ResponseEntity<Resource> download(@RequestParam String fileName) throws IOException {

        var file = pdfHandler.getFileForDownload(fileName);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=download.pdf")
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
