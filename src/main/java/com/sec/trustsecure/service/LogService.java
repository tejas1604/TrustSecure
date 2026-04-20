package com.sec.trustsecure.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.sec.trustsecure.entity.FirewallLog;
import com.sec.trustsecure.repository.FirewallLogRepository;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class LogService {

    private volatile boolean processingDone = false;

    @Autowired
    private FirewallLogRepository repository;

    private final WebClient webClient = WebClient.builder().build();

    private final String VT_API_KEY = "9fa9d40f208878578bd11f84f134b08625f1efff96cd957fe7a0beec5dde1367";

    public Mono<Void> processFile(MultipartFile file) {

        processingDone = false;

        return Mono.fromRunnable(() -> {

            try {
                BufferedReader bf = new BufferedReader(
                        new InputStreamReader(file.getInputStream())
                );

                String line;
                int count = 0;

                List<Mono<FirewallLog>> logMonos = new ArrayList<>();

                while ((line = bf.readLine()) != null && count != 20) {

                    if (line.startsWith("#")) continue;

                    String[] parts = line.trim().split("\\s+");

                    if (parts.length < 8) continue;

                    count++;

                    FirewallLog log = new FirewallLog(
                            parts[0],
                            parts[1],
                            parts[2],
                            parts[3],
                            parts[4],
                            parts[5],
                            parts[6],
                            parts[7]
                    );

                    String ip = parts[6];

                    Mono<FirewallLog> logMono = calculateRiskFromAPI(ip)
                            .map(score -> {
                                log.setRisk(String.valueOf(score));
                                return log;
                            });

                    logMonos.add(logMono);
                }

                List<FirewallLog> finalLogs = Mono.zip(logMonos, arr -> {
                    List<FirewallLog> logs = new ArrayList<>();
                    for (Object obj : arr) {
                        logs.add((FirewallLog) obj);
                    }
                    return logs;
                })
                .timeout(Duration.ofSeconds(10))
                .onErrorReturn(new ArrayList<>())
                .block();

                if (finalLogs != null && !finalLogs.isEmpty()) {
                    repository.saveAll(finalLogs);
                }

                processingDone = true;

                bf.close();

                System.out.println("Processing complete");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    // ================= RISK =================

    private Mono<Double> calculateRiskFromAPI(String ip) {

        Mono<Double> vt = getVirusTotalScore(ip);
        Mono<Double> abuse = getAbuseIPDBScore(ip);

        return Mono.zip(vt, abuse)
                .map(tuple -> {

                    double vtScore = tuple.getT1();
                    double abuseScore = tuple.getT2();

                    return (vtScore * 0.6) + (abuseScore * 0.4);
                })
                .onErrorReturn(0.0);
    	}

    // ================= VIRUSTOTAL =================

    private Mono<Double> getVirusTotalScore(String ip) {

        return webClient.get()
                .uri("https://www.virustotal.com/api/v3/ip_addresses/" + ip)
                .header("x-apikey", VT_API_KEY)
                .retrieve()
                .bodyToMono(Map.class)
                .map(res -> {

                    Map data = (Map) res.get("data");
                    Map attr = (Map) data.get("attributes");
                    Map stats = (Map) attr.get("last_analysis_stats");

                    int malicious = (int) stats.get("malicious");
                    int suspicious = (int) stats.get("suspicious");
                    int harmless = (int) stats.get("harmless");
                    int undetected = (int) stats.get("undetected");

                    int total = malicious + suspicious + harmless + undetected;

                    if (total == 0) return 0.0;

                    return (malicious * 100.0) / total;
                })
                .timeout(Duration.ofSeconds(5))
                .onErrorReturn(0.0);
    }
    
    private Mono<Double> getAbuseIPDBScore(String ip) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.abuseipdb.com")
                        .path("/api/v2/check")
                        .queryParam("ipAddress", ip)
                        .queryParam("maxAgeInDays", "90")
                        .build())
                .header("Key", "4ecea4986088f7ee118a7fe3ecba6d3c41cb7dc85fd55e62582e1691fe341d7e8389934bddda1c19")
                .header("Accept", "application/json")
                .retrieve()
                .bodyToMono(Map.class)
                .map(res -> {
                    Map data = (Map) res.get("data");
                    return ((Number) data.get("abuseConfidenceScore")).doubleValue();
                })
                .onErrorReturn(0.0);
    }
    
    public boolean isProcessingDone() {
        return processingDone;
    }
}