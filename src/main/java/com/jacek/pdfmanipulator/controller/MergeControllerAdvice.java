package com.jacek.pdfmanipulator.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class MergeControllerAdvice {

    private final static Logger LOG = LogManager.getLogger(MergeControllerAdvice.class);

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handle(Model model, Exception ex) {
        LOG.error("Uploaded file is too big!", ex);
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
}
