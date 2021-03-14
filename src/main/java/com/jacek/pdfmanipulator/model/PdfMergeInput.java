package com.jacek.pdfmanipulator.model;

import java.io.File;

public class PdfMergeInput {

    private File file1;
    private File file2;
    private String fileName;



    public File getFile1() {
        return file1;
    }

    public void setFile1(File file1) {
        this.file1 = file1;
    }

    public File getFile2() {
        return file2;
    }

    public void setFile2(File file2) {
        this.file2 = file2;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "PdfMergeInput{" +
                "file1=" + file1 +
                ", file2=" + file2 +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
