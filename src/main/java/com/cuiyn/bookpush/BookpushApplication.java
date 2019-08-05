package com.cuiyn.bookpush;

import com.cuiyn.bookpush.config.EmailServerConfig;
import com.cuiyn.bookpush.config.SiteConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({SiteConfig.class, EmailServerConfig.class})
public class BookpushApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookpushApplication.class, args);
    }

}
