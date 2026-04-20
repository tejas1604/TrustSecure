package com.sec.trustsecure.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sec.trustsecure.entity.FirewallLog;
import com.sec.trustsecure.repository.FirewallLogRepository;

@RestController
public class LogController {

    @Autowired
    private FirewallLogRepository repository;

    @GetMapping("/logs")
    public List<FirewallLog> getLogs() {
        return repository.findAll();
    }
}