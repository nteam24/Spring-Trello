package com.sparta.springusersetting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringUsersettingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringUsersettingApplication.class, args);
    }

}
