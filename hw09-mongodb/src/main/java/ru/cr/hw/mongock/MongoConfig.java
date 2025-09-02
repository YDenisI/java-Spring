package ru.cr.hw.mongock;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.embedded.port:27017}")  // Значение по умолчанию
    private int port;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:" + port);
    }
}