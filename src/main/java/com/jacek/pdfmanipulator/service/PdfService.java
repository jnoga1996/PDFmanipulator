package com.jacek.pdfmanipulator.service;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class PdfService {

    private final static String FILE_DIRECTORY = "src/main/resources/out/";

    public void merge(String fileName1, String fileName2, String mergedFileName) throws IOException {
        File file1 = new File(FILE_DIRECTORY + fileName1);
        File file2 = new File(FILE_DIRECTORY + fileName2);

        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
        pdfMergerUtility.setDestinationFileName(FILE_DIRECTORY + mergedFileName);

        pdfMergerUtility.addSource(file1);
        pdfMergerUtility.addSource(file2);

        pdfMergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
    }

    public File getMergedFile(String fileName) {
        if (fileName == null) {
            throw new IllegalStateException("File name is null!");
        }
        File mergedFile = new File(FILE_DIRECTORY + fileName);
        System.out.println("Fetched file: " + mergedFile.getName());
        return mergedFile;
    }

}
