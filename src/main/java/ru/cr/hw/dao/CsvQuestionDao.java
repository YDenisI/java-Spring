package ru.cr.hw.dao;


import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.cr.hw.config.TestFileNameProvider;
import ru.cr.hw.dao.dto.QuestionDto;
import ru.cr.hw.domain.Question;
import ru.cr.hw.exceptions.QuestionReadException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        String fileName = fileNameProvider.getTestFileName();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new QuestionReadException("File not found: " + fileName);
            }
            List<String> linesForParsing = readFileQuestions(is);

            String dataForCsv = String.join("\n", linesForParsing);
            List<QuestionDto> questionDtos = parseFileQuestions(dataForCsv);
            List<Question> domainObjects = convertQuestionToDomain(questionDtos);
            return domainObjects;
        } catch (Exception e) {
            throw new QuestionReadException("Error reading questions from file: " + e.getMessage(), e);
        }
    }

    public List<String> readFileQuestions(InputStream is) throws IOException {
        List<String> linesForParsing = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("#")) {
                    continue;
                }
                linesForParsing.add(line);
            }
        }
        return linesForParsing;
    }

    public List<QuestionDto> parseFileQuestions(String dataForCsv) throws IOException {
        List<QuestionDto> questionDtos;
        try (BufferedReader csvReader = new BufferedReader(new StringReader(dataForCsv))) {
            questionDtos = new CsvToBeanBuilder<QuestionDto>(csvReader)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .build()
                    .parse();
        }
        return questionDtos;
    }

    public List<Question> convertQuestionToDomain(List<QuestionDto> questionDtos) {
        List<Question> domainObjects = new ArrayList<>();
        for (QuestionDto questionDto : questionDtos) {
            domainObjects.add(questionDto.toDomainObject());
        }
        return  domainObjects;
    }
}
