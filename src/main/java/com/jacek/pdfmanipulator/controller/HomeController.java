package com.jacek.pdfmanipulator.controller;

import com.jacek.pdfmanipulator.model.PdfMergeInput;
import com.jacek.pdfmanipulator.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;

@Controller
@RequestMapping("/home")
public class HomeController {

    private PdfService pdfService;

    @Autowired
    public HomeController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/merge")
    public String index(Model model) {
        model.addAttribute("pdfMergeInput", new PdfMergeInput());
        return "index";
    }

    @PostMapping(value = "/merge", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] merge(@ModelAttribute PdfMergeInput pdfMergeInput) throws Exception {
        System.out.println(pdfMergeInput);
        if (pdfMergeInput != null) {
            String fileName1 = pdfMergeInput.getFile1().getName();
            String fileName2 = pdfMergeInput.getFile2().getName();
            String resultFileName = pdfMergeInput.getFileName();
            if (isEmpty(fileName1) || isEmpty(fileName2) || isEmpty(resultFileName)) {
                System.out.println("One of file names is empty!");
            }

            resultFileName += ".pdf";
            pdfService.merge(fileName1, fileName2, resultFileName);
            File mergedFile = pdfService.getMergedFile(resultFileName);
            return Files.readAllBytes(mergedFile.toPath());
        }

        return null;
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception ex) {
        System.out.println(ex);
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    private static boolean isEmpty(String name) {
        return name == null || name.isEmpty();
    }
}
