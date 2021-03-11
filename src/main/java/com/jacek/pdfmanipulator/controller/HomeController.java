package com.jacek.pdfmanipulator.controller;

import com.jacek.pdfmanipulator.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private PdfService pdfService;

    @Autowired
    public HomeController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/merge")
    public String merge() throws Exception {
        pdfService.merge("test1.pdf", "test2.pdf", "result.pdf");
        return "index";
    }
}
