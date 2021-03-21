package com.jacek.pdfmanipulator;

import com.jacek.pdfmanipulator.service.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PdfmanipulatorApplication implements CommandLineRunner {

	FileStorageService fileStorageService;

	public PdfmanipulatorApplication(FileStorageService fileStorageService) {
		this.fileStorageService = fileStorageService;
	}

	public static void main(String[] args) {
		SpringApplication.run(PdfmanipulatorApplication.class, args);
	}

	@Override
	public void run(String... args) {
		fileStorageService.deleteAll();
		fileStorageService.init();
	}
}
