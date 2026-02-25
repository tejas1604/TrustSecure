package com.sec.trustsecure;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sec.trustsecure.repository.FirewallLogRepository;
import com.sec.trustsecure.entity.FirewallLog;
import java.io.BufferedReader;
@SpringBootApplication
public class TrustsecureApplication implements CommandLineRunner {

    private final FirewallLogRepository repository;

    public TrustsecureApplication(FirewallLogRepository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(TrustsecureApplication.class, args);
    }

    @Override
    public void run(String... args) {
    	
    	BufferedReader bf = new BufferedReader("C:/Windows/System32/LogFiles/firewall/pfirewall.log");
    	
        FirewallLog log = new FirewallLog(
                "2026-02-22",
                "13:30",
                "192.168.1.10",
                "443",
                "10.0.0.5",
                "51515",
                "ALLOW",
                "12345"
        );

        repository.save(log);

        System.out.println("Inserted successfully.");
        System.out.println("Total rows in DB: " + repository.count());
    }
}