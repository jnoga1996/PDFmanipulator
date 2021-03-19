package com.jacek.pdfmanipulator.validator;

import java.io.File;

public class FileValidator {

    private final static String PDF_EXTENSION = ".PDF";
    private File file;
    private String message;

    public FileValidator(File file) {
        this.file = file;
        this.message = "";
    }

    public boolean isValid() {
        if (file == null) {
            message = "File is null!";
            return false;
        }
        if (!isPDF(file)) {
            message = "File has incorrect format or file name is null!";
            return false;
        }

        return true;
    }

    public String getMessage() {
        return message;
    }

    private boolean isPDF(File file) {
        String fileName = file.getName();
        return !isEmpty(fileName) && fileName.toUpperCase().contains(PDF_EXTENSION);
    }

    public static boolean isEmpty(String name) {
        return name == null || name.isEmpty();
    }
}
