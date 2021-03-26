package com.jacek.pdfmanipulator.controller;

import com.jacek.pdfmanipulator.service.FileStorageService;
import com.jacek.pdfmanipulator.service.PdfService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/merge")
public class MergeController {

    private PdfService pdfService;
    private FileStorageService fileStorageService;

    private final static Logger LOG = LogManager.getLogger(MergeController.class);

    @Autowired
    public MergeController(PdfService pdfService, FileStorageService fileStorageService) {
        this.pdfService = pdfService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] merge(@RequestParam("file1") MultipartFile uploadedFile1,
                                      @RequestParam("file2") MultipartFile uploadedFile2) throws Exception {

        return pdfService.process(uploadedFile1, uploadedFile2);
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception ex) {
        LOG.error(ex.getMessage(), ex);
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

}
