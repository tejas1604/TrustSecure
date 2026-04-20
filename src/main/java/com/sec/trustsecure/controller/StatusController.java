package com.sec.trustsecure.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sec.trustsecure.entity.FirewallLog;
import com.sec.trustsecure.repository.FirewallLogRepository;
import com.sec.trustsecure.service.LogService;
@RestController
public class StatusController {

    @Autowired
    private LogService service;

    @GetMapping("/status")
    public boolean getStatus() {
        return service.isProcessingDone();
    }
}