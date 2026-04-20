package com.sec.trustsecure.configuration;


import javax.sql.DataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class config {

    @Bean
    CommandLineRunner probe(DataSource dataSource) {
        return args -> {
            System.out.println("DB CONNECTION = " + dataSource.getConnection());
        };
    }
}