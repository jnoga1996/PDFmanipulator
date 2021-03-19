package com.jacek.pdfmanipulator.service;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PdfService {

    private final static Path FILE_DIRECTORY = Paths.get("storage");

    public void merge(String fileName1, String fileName2, String mergedFileName) throws IOException {
        File file1 = new File(generatePathToFile(fileName1));
        File file2 = new File(generatePathToFile(fileName2));

        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
        String destinationPath = generatePathToFile(mergedFileName);
        System.out.println("Generating file:" + destinationPath);
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
            throw new IllegalStateException("File name is null!");
        }
        File mergedFile = new File(generatePathToFile(fileName));
        System.out.println("Fetched file: " + mergedFile.getName());
        return mergedFile;
    }

}
