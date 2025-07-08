package ru.cr.hw.config;

import lombok.Data;

@Data
public class AppProperties implements TestConfig, TestFileNameProvider {

    private int rightAnswersCountToPass;

    private String testFileName;
}
