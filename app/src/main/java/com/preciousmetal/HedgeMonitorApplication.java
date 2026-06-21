package com.preciousmetal;

import com.preciousmetal.risk.alert.AlertConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AlertConfig.class)
public class HedgeMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(HedgeMonitorApplication.class, args);
    }
}
