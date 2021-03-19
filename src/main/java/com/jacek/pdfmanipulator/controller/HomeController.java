package com.jacek.pdfmanipulator.controller;

import com.jacek.pdfmanipulator.model.PdfMergeInput;
import com.jacek.pdfmanipulator.service.FileStorageService;
import com.jacek.pdfmanipulator.service.PdfService;
import com.jacek.pdfmanipulator.validator.FileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;

@Controller
@RequestMapping("/home")
public class HomeController {

    private PdfService pdfService;
    private FileStorageService fileStorageService;

    @Autowired
    public HomeController(PdfService pdfService, FileStorageService fileStorageService) {
        this.pdfService = pdfService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/merge")
    public String index(Model model) {
        model.addAttribute("pdfMergeInput", new PdfMergeInput());
        return "index";
    }

    @PostMapping(value = "/merge", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] merge(@RequestParam("file1") MultipartFile uploadedFile1,
                                      @RequestParam("file2") MultipartFile uploadedFile2,
                                      @RequestParam("resultFileName") String resultFileName) throws Exception {

        System.out.println(uploadedFile1.getOriginalFilename());
        System.out.println(uploadedFile2.getOriginalFilename());
        fileStorageService.store(uploadedFile1);
        fileStorageService.store(uploadedFile2);
        File file1 = fileStorageService.loadFile(uploadedFile1.getOriginalFilename()).getFile();
        File file2 = fileStorageService.loadFile(uploadedFile2.getOriginalFilename()).getFile();
        FileValidator file1Validator = new FileValidator(file1);
        FileValidator file2Validator = new FileValidator(file2);
        if (!file1Validator.isValid()) {
            String message = "First file is incorrect! " + file1Validator.getMessage();
            System.out.println(message);
            throw new IllegalStateException(message);
        }
        if (!file2Validator.isValid()) {
            String message = "Second file is incorrect! " + file2Validator.getMessage();
            System.out.println(message);
            throw new IllegalStateException(message);
        }

        if (FileValidator.isEmpty(resultFileName)) {
            String message = "Result file name is empty!";
            System.out.println(message);
            throw new IllegalStateException(message);
        }

        if (!resultFileName.toUpperCase().contains(".PDF")) {
            resultFileName += ".PDF";
        }

        pdfService.merge(file1.getName(), file2.getName(), resultFileName);
        File mergedFile = pdfService.getMergedFile(resultFileName);
        return Files.readAllBytes(mergedFile.toPath());
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception ex) {
        System.out.println(ex);
        model.addAttribute("error", ex.getMessage());
        return "error";
    }


}
