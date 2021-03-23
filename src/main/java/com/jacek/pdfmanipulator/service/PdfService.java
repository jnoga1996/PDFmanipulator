package com.jacek.pdfmanipulator.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PdfService {

    private final static Logger LOG = LogManager.getLogger(PdfService.class);
    private final static Path FILE_DIRECTORY = Paths.get("storage");

    public void merge(String fileName1, String fileName2, String mergedFileName) throws IOException {
        File file1 = new File(generatePathToFile(fileName1));
        File file2 = new File(generatePathToFile(fileName2));

        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
        String destinationPath = generatePathToFile(mergedFileName);
        LOG.info("Generating file:" + destinationPath);
        pdfMergerUtility.setDestinationFileName(destinationPath);

        pdfMergerUtility.addSource(file1);
        pdfMergerUtility.addSource(file2);

        pdfMergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
    }

    private String generatePathToFile(String fileName) {
        return FILE_DIRECTORY + "/" + fileName;
    }

    public File getMergedFile(String fileName) {
        if (fileName == null) {
            String error = "File name is null!";
            LOG.warn(error);
            throw new IllegalStateException(error);
        }
        File mergedFile = new File(generatePathToFile(fileName));
        LOG.info("Fetched file: " + mergedFile.getName());
        return mergedFile;
    }

}
