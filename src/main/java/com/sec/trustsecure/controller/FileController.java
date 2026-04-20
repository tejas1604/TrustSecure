package com.sec.trustsecure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sec.trustsecure.service.LogService;

import reactor.core.publisher.Mono;

@RestController
public class FileController {

    @Autowired
    private LogService service;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {

        service.processFile(file).subscribe();   

        return "Processing started...";
    }
}