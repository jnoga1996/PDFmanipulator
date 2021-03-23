package com.jacek.pdfmanipulator.service;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Stream;

public interface FileStorage {

    void store(MultipartFile file, int securityHash);

    Resource loadFile(String filename, int securityHash);

    void deleteAll();

    void delete(String fileName);

    void init();

    Stream getFiles();
}
