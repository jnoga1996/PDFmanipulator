package com.jacek.pdfmanipulator.service;

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

    public FileStorageService() {
        init();
    }

    @Override
    public void store(MultipartFile file) {
        try {
            Path targetPath = rootLocation.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error occurred while saving file:" + file.getOriginalFilename() + "\n" + e.getMessage());
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
                throw new RuntimeException("File does not exist or is not readable!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error while loading file: " + filename + "!\n" + e.getMessage());
        }
    }

    @Override
    public void delete(String fileName) {
        try {
            String target = getTargetFileName(fileName);
            FileSystemUtils.deleteRecursively(Paths.get(target));
        } catch (IOException e) {
            System.out.println("Error while removing file: " + fileName);
        }
    }

    @Override
    public void deleteAll() {
        try {
            FileSystemUtils.deleteRecursively(rootLocation);
        } catch (IOException e) {
            System.out.println("Error when removing storage folder!\n " + e.getMessage());
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            System.out.println("Error when initializing storage!\n " + e.getMessage());
        }
    }

    @Override
    public Stream getFiles() {
        try {
            return Files.walk(rootLocation, 1).filter(path -> !path.equals(rootLocation))
                    .map(rootLocation::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Error while getting files!\n" + e.getMessage());
        }
    }

    private String getTargetFileName(String fileName) {
        return rootLocation.toString() + "/" + fileName;
    }
}
