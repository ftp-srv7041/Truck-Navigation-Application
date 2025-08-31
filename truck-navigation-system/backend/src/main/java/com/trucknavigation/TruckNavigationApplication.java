package com.trucknavigation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TruckNavigationApplication {

    public static void main(String[] args) {
        SpringApplication.run(TruckNavigationApplication.class, args);
    }
}
