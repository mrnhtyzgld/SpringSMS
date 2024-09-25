package org.example.springsms;

import org.springframework.boot.SpringApplication;

public class TestSpringSmsApplication {

    public static void main(String[] args) {
        SpringApplication.from(SpringSmsApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
