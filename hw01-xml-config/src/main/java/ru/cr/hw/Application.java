package ru.cr.hw;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.cr.hw.service.TestRunnerService;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context  = new ClassPathXmlApplicationContext("/spring-context.xml");
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();
    }
}
