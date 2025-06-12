package br.com.DataPilots.Fileflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FileflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileflowApplication.class, args);
    }

}