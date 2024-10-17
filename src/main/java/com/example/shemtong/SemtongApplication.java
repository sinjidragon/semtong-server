package com.example.shemtong;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SemtongApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        System.out.println(dotenv.get("DB_URL"));
        SpringApplication.run(SemtongApplication.class, args);
    }

}
