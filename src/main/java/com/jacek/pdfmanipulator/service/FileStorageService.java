package com.jacek.pdfmanipulator.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileStorageService implements FileStorage {

    private final Path rootLocation = Paths.get("storage");

    private final static Logger LOG = LogManager.getLogger(FileStorageService.class);

    @Override
    public void store(MultipartFile file) {
        try {
            Path targetPath = rootLocation.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            LOG.error("Error occurred while saving file:" + file.getOriginalFilename(), ex);
        }
    }

    @Override
    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                String error = "File does not exist or is not readable!";
                LOG.error(error);
                throw new RuntimeException(error);
            }
        } catch (MalformedURLException e) {
            String error = "Error while loading file: " + filename + "!";
            LOG.error(error, e);
            throw new RuntimeException(error);
        }
    }

    @Override
    public void delete(String fileName) {
        try {
            String target = getTargetFileName(fileName);
            FileSystemUtils.deleteRecursively(Paths.get(target));
        } catch (IOException ex) {
            LOG.error("Error while removing file: " + fileName, ex);
        }
    }

    @Override
    public void deleteAll() {
        try {
            FileSystemUtils.deleteRecursively(rootLocation);
        } catch (IOException ex) {
            LOG.error("Error when removing storage folder!", ex);
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException ex) {
            LOG.error("Error when initializing storage!", ex);
        }
    }

    @Override
    public Stream getFiles() {
        try {
            return Files.walk(rootLocation, 1).filter(path -> !path.equals(rootLocation))
                    .map(rootLocation::relativize);
        } catch (IOException ex) {
            throw new RuntimeException("Error while getting files!");
        }
    }

    private String getTargetFileName(String fileName) {
        return rootLocation.toString() + "/" + fileName;
    }
}
