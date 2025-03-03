package br.com.DataPilots.Fileflow.services;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneOffset;

@Configuration
public class TimeConfig {
    private final String OFFSET_ID = "-03:00";

    @Bean
    public ZoneOffset zoneOffset() {
        return ZoneOffset.of(OFFSET_ID);
    }
}