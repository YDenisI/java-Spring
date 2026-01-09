package ru.cr.hw.dao;


import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.cr.hw.config.TestFileNameProvider;
import ru.cr.hw.dao.dto.QuestionDto;
import ru.cr.hw.domain.Question;
import ru.cr.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        String fileName = fileNameProvider.getTestFileName();

        if (fileName == null) {
            throw new IllegalArgumentException("FileName is null");
        }

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new QuestionReadException("File not found: " + fileName);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                List<QuestionDto> questionDtos = parseFileQuestions(reader);
                List<Question> domainObjects = convertQuestionToDomain(questionDtos);
                return domainObjects;
            }
        } catch (IOException e) {
            throw new QuestionReadException("Error reading questions from file: " + e.getMessage(), e);
        }
    }

    public List<QuestionDto> parseFileQuestions(BufferedReader csvReader) throws IOException {
        return new CsvToBeanBuilder<QuestionDto>(csvReader)
                .withType(QuestionDto.class)
                .withSeparator(';')
                .withSkipLines(1)
                .build()
                .parse();
    }

    public List<Question> convertQuestionToDomain(List<QuestionDto> questionDtos) {
        List<Question> domainObjects = new ArrayList<>();
        for (QuestionDto questionDto : questionDtos) {
            domainObjects.add(questionDto.toDomainObject());
        }
        return  domainObjects;
    }
}
