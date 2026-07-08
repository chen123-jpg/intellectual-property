package com.chen.intellectualproperty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class IntellectualPropertyApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntellectualPropertyApplication.class, args);
    }

}
