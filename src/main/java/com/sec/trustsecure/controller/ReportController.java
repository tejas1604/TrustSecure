package com.sec.trustsecure.controller;

import com.sec.trustsecure.repository.FirewallLogRepository;
import com.sec.trustsecure.entity.FirewallLog;
import com.sec.trustsecure.service.ChatGPTService;
import com.sec.trustsecure.service.ImageService;
import com.sec.trustsecure.service.PdfService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {
	private final ImageService imageService;
    private final FirewallLogRepository repository;
    private final ChatGPTService chatGPTService;
    

    public ReportController(FirewallLogRepository repository,
                            ChatGPTService chatGPTService,
                            ImageService imageService) {
        this.repository = repository;
        this.chatGPTService = chatGPTService;
        this.imageService = imageService;
    }

    @GetMapping("/generate")
    public Mono<ResponseEntity<byte[]>> generateReport() {

        List<FirewallLog> logs = repository.findAll();

        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze these firewall logs and give a short report:\n\n");

        for (FirewallLog log : logs) {
            prompt.append("IP: ").append(log.getField5())
                  .append(", Risk: ").append(log.getRisk())
                  .append("\n");
        }

        return chatGPTService.generateReport(prompt.toString())
                .map(response -> {

                    byte[] image = imageService.generateImage(response);

                    return ResponseEntity.ok()
                            .header("Content-Disposition", "attachment; filename=report.png")
                            .contentType(MediaType.IMAGE_PNG)
                            .body(image);
                });
    }
}