package com.jacek.pdfmanipulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PdfmanipulatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdfmanipulatorApplication.class, args);
	}

}
