package ru.cr.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.cr.hw.service.TestRunnerService;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
     SpringApplication.run(Application.class, args);
    }
}