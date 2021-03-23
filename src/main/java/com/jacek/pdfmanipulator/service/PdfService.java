package com.jacek.pdfmanipulator.service;

import com.jacek.pdfmanipulator.model.MergeInput;
import com.jacek.pdfmanipulator.validator.FileValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;

@Service
public class PdfService {

    private final static Logger LOG = LogManager.getLogger(PdfService.class);
    private final static Path FILE_DIRECTORY = Paths.get("storage");

    private FileStorageService fileStorageService;
    private SecureRandom secureRandom;

    @Autowired
    public PdfService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
        this.secureRandom = new SecureRandom();

    }

    public byte[] process(MultipartFile uploadedFile1, MultipartFile uploadedFile2) throws IOException {
        MergeInput input = storeAndLoadUploadedFiles(uploadedFile1, uploadedFile2);

        if (input == null) {
            String message = "MergeInput is null!";
            LOG.error(message);
            throw new IllegalStateException(message);
        }
        FileValidator fileValidator = new FileValidator(input);

        if (!fileValidator.isValid()) {
            String message = fileValidator.getMessage();
            LOG.warn(message);
            throw new IllegalStateException(message);
        }

        String resultFileName = secureRandom.nextInt() + ".PDF";

        if (FileValidator.isEmpty(resultFileName)) {
            String message = "Result file name is empty!";
            LOG.warn(message);
            throw new IllegalStateException(message);
        }

        merge(input.getFile1().getName(), input.getFile2().getName(), resultFileName);
        File mergedFile = getMergedFile(resultFileName);

        return Files.readAllBytes(mergedFile.toPath());
    }

    private MergeInput storeAndLoadUploadedFiles(MultipartFile uploadedFile1, MultipartFile uploadedFile2) throws IOException {
        int securityHash1 = secureRandom.nextInt();
        int securityHash2 = secureRandom.nextInt();
        LOG.info("File1: " + uploadedFile1.getOriginalFilename() + ", securityHash: " + securityHash1);
        LOG.info("File2: " + uploadedFile2.getOriginalFilename() + ", securityHash: " + securityHash2);
        fileStorageService.store(uploadedFile1, securityHash1);
        fileStorageService.store(uploadedFile2, securityHash2);
        File file1 = loadFileFromResource(uploadedFile1, securityHash1);
        File file2 = loadFileFromResource(uploadedFile2, securityHash2);

        return new MergeInput(file1, file2, securityHash1, securityHash2);
    }

    private File loadFileFromResource(MultipartFile multipartFile, int securityHash) throws IOException {
        Resource resource = fileStorageService.loadFile(multipartFile.getOriginalFilename(), securityHash);
        if (resource == null) {
            throw new IllegalStateException();
        }
        return resource.getFile();
    }

    public void merge(String fileName1, String fileName2, String mergedFileName) throws IOException {
        File file1 = new File(generatePathToFile(fileName1));
        File file2 = new File(generatePathToFile(fileName2));

        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
        String destinationPath = generatePathToFile(mergedFileName);
        LOG.info("Generating file: " + destinationPath);
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
