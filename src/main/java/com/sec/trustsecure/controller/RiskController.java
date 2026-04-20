package com.sec.trustsecure.controller;

import com.sec.trustsecure.service.RiskService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/risk")
public class RiskController {

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    @GetMapping("/{ip}")
    public Mono<Map<String, Object>> getRisk(@PathVariable String ip) {
        return riskService.getRiskScore(ip);
    }
}