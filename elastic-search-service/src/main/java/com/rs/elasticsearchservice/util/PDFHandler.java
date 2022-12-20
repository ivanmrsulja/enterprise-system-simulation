package com.rs.elasticsearchservice.util;

import com.rs.elasticsearchservice.model.CandidateApplication;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class PDFHandler {

    private final String DATA_DIR_PATH = "src/main/resources/data";

    public CandidateApplication getWithIndexedDocuments(String cvFileName, String letterFileName) throws IOException {
        CandidateApplication retVal = new CandidateApplication();

        String cvPath = Paths.get(DATA_DIR_PATH + File.separator + cvFileName).toString();
        String letterPath = Paths.get(DATA_DIR_PATH + File.separator + letterFileName).toString();

        File cv = new File(cvPath);
        File letter = new File(letterPath);

        PDFParser parserCV = new PDFParser(new RandomAccessFile(cv, "r"));
        parserCV.parse();
        retVal.setCv(getText(parserCV));
        retVal.setCvPath(cvFileName);

        PDFParser parserLetter = new PDFParser(new RandomAccessFile(letter, "r"));
        parserLetter.parse();
        retVal.setLetter(getText(parserLetter));
        retVal.setLetterPath(letterFileName);

        return retVal;
    }

    public String getText(PDFParser parser) throws IOException {
        PDFTextStripper textStripper = new PDFTextStripper();
        return textStripper.getText(parser.getPDDocument());
    }

    public String saveUploadedFile(MultipartFile file) throws IOException {
        String fileName = null;
        if (! file.isEmpty()) {
            fileName = UUID.randomUUID() + ".pdf";
            byte[] bytes = file.getBytes();
            Path path = Paths.get(DATA_DIR_PATH + File.separator + fileName);
            Files.write(path, bytes);
        }
        return fileName;
    }
}
