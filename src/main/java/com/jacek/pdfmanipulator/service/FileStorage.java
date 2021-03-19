package com.jacek.pdfmanipulator.service;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Stream;

public interface FileStorage {

    void store(MultipartFile file);

    Resource loadFile(String filename);

    void deleteAll();

    void init();

    Stream getFiles();
}
