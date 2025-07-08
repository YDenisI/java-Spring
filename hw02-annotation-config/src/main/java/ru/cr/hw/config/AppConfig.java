package ru.cr.hw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.cr.hw.dao.CsvQuestionDao;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public AppProperties appProperties(@Value("${test.rightAnswersCountToPass}") int rightAnswersCountToPass,
                                       @Value("${test.fileName}") String testFileName) {
        AppProperties props = new AppProperties();
        props.setRightAnswersCountToPass(rightAnswersCountToPass);
        props.setTestFileName(testFileName);
        return props;
    }

    @Bean
    public CsvQuestionDao questionDao(AppProperties appProperties) {
        return new CsvQuestionDao(appProperties);
    }
}
