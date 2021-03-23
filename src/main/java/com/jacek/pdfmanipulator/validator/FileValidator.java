package com.jacek.pdfmanipulator.validator;

import com.jacek.pdfmanipulator.model.MergeInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileValidator {

    private final static Logger LOG = LogManager.getLogger(FileValidator.class);
    private final static String PDF_EXTENSION = ".PDF";
    private final static long MB_IN_BYTES = 1000000;
    private final static long MAX_FILE_SIZE_IN_MB = MB_IN_BYTES * 5;
    private MergeInput input;
    private String message;
    private StringBuffer stringBuffer;

    public FileValidator(MergeInput input) {
        this.input = input;
        this.message = "";
        this.stringBuffer = new StringBuffer();
    }

    public boolean isValid() {
        return isValid(input.getFile1()) && isValid(input.getFile2());
    }

    public boolean isValid(File file) {
        LOG.info("Validating file: " + file.getName());
        if (file == null) {
            message = "File is null!\n";
            stringBuffer.append(message);
            LOG.warn(message);
            return false;
        }

        if (!isPDF(file)) {
            message = "File has incorrect format or file name is null!\n";
            LOG.warn(message);
            stringBuffer.append(message);
            return false;
        }

        if (!isSizeOK(file)) {
            message = "File is too big!\n";
            LOG.warn(message);
            stringBuffer.append(message);
            return false;
        }

        return true;
    }

    public String getMessage() {
        return stringBuffer.toString();
    }

    private boolean isPDF(File file) {
        String fileName = file.getName();
        return !isEmpty(fileName) && fileName.toUpperCase().contains(PDF_EXTENSION);
    }

    private boolean isSizeOK(File file) {
        try {
            Path path = Paths.get(file.getPath());
            long size = Files.size(path);
            LOG.info("File: " + file.getName() + " has size of: " + size + " bytes");
            return size > 0 && size <= MAX_FILE_SIZE_IN_MB;
        } catch (IOException ex) {
            LOG.error("Could not read file size!");
            return false;
        }
    }

    public static boolean isEmpty(String name) {
        return name == null || name.isEmpty();
    }
}
