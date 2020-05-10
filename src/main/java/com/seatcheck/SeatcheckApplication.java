package com.seatcheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author wjjj
 */
@SpringBootApplication

public class SeatcheckApplication{
//        extends SpringBootServletInitializer {
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(SeatcheckApplication.class);
//    }

    public static void main(String[] args) {
        SpringApplication.run(SeatcheckApplication.class, args);
    }

}
