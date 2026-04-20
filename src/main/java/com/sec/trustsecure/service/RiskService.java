package com.sec.trustsecure.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class RiskService {

    private final WebClient webClient;

    private final String VT_API_KEY = "9fa9d40f208878578bd11f84f134b08625f1efff96cd957fe7a0beec5dde1367";
    private final String ABUSE_API_KEY = "4ecea4986088f7ee118a7fe3ecba6d3c41cb7dc85fd55e62582e1691fe341d7e8389934bddda1c19";

    public RiskService() {
        this.webClient = WebClient.builder().build();
    }

    public Mono<Map<String, Object>> getRiskScore(String ip) {

        Mono<Double> vtScoreMono = getVirusTotalScore(ip);
        Mono<Double> abuseScoreMono = getAbuseIPDBScore(ip);

        return Mono.zip(vtScoreMono, abuseScoreMono)
                .map(tuple -> {

                    double vtScore = tuple.getT1();
                    double abuseScore = tuple.getT2();

                    double finalScore = (vtScore * 0.6) + (abuseScore * 0.4);

                    String category = classify(finalScore);

                    return Map.of(
                            "ip", ip,
                            "virusTotalScore", vtScore,
                            "abuseIPDBScore", abuseScore,
                            "finalScore", finalScore,
                            "category", category
                    );
                });
    }

    // ================= VIRUSTOTAL =================

    private Mono<Double> getVirusTotalScore(String ip) {

        return webClient.get()
                .uri("https://www.virustotal.com/api/v3/ip_addresses/" + ip)
                .header("x-apikey", VT_API_KEY)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {

                    Map data = (Map) response.get("data");
                    Map attributes = (Map) data.get("attributes");
                    Map stats = (Map) attributes.get("last_analysis_stats");

                    int malicious = (int) stats.get("malicious");
                    int suspicious = (int) stats.get("suspicious");
                    int harmless = (int) stats.get("harmless");
                    int undetected = (int) stats.get("undetected");

                    int total = malicious + suspicious + harmless + undetected;

                    if (total == 0) return 0.0;

                    return (malicious * 100.0) / total;
                })
                .onErrorReturn(0.0); // fallback if API fails
    }

    // ================= ABUSEIPDB =================

    private Mono<Double> getAbuseIPDBScore(String ip) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.abuseipdb.com")
                        .path("/api/v2/check")
                        .queryParam("ipAddress", ip)
                        .queryParam("maxAgeInDays", "90")
                        .build())
                .header("Key", ABUSE_API_KEY)
                .header("Accept", "application/json")
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {

                    Map data = (Map) response.get("data");

                    return ((Number) data.get("abuseConfidenceScore")).doubleValue();
                })
                .onErrorReturn(0.0);
    }

    // ================= CLASSIFICATION =================

    private String classify(double score) {
        if (score < 20) return "SAFE";
        else if (score < 50) return "SUSPICIOUS";
        else if (score < 80) return "RISKY";
        else return "MALICIOUS";
    }
}	