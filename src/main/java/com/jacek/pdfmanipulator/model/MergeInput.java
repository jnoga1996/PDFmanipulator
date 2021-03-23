package com.jacek.pdfmanipulator.model;

import java.io.File;

public class MergeInput {

    private File file1;
    private File file2;
    private int securityHash1;
    private int securityHash2;

    public MergeInput(File file1, File file2, int securityHash1, int securityHash2) {
        this.file1 = file1;
        this.file2 = file2;
        this.securityHash1 = securityHash1;
        this.securityHash2 = securityHash2;
    }

    public File getFile1() {
        return file1;
    }

    public File getFile2() {
        return file2;
    }

    public int getSecurityHash1() {
        return securityHash1;
    }

    public int getSecurityHash2() {
        return securityHash2;
    }
}
