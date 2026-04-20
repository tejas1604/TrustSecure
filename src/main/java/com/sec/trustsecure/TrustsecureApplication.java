package com.sec.trustsecure;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sec.trustsecure.repository.FirewallLogRepository;
import com.sec.trustsecure.entity.FirewallLog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

@SpringBootApplication
public class TrustsecureApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TrustsecureApplication.class, args);
    }

    private final FirewallLogRepository repository;

    public TrustsecureApplication(FirewallLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        System.out.println("App started. Waiting for file upload...");
    }
}