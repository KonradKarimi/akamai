package com.konradkarimi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class AkamaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AkamaiApplication.class, args);
    }

}
