package ru.cr.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.cr.hw.config.TestFileNameProvider;
import ru.cr.hw.dao.dto.QuestionDto;
import ru.cr.hw.domain.Question;
import ru.cr.hw.exceptions.QuestionReadException;
import ru.cr.hw.service.LocalizedMessagesService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


//@RequiredArgsConstructor
@Component
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    private final LocalizedMessagesService localizedMessagesService;

    @Autowired
    public CsvQuestionDao(TestFileNameProvider fileNameProvider,
                          @Qualifier("localizedMessagesServiceImpl") LocalizedMessagesService localizedMessagesService) {
        this.fileNameProvider = fileNameProvider;
        this.localizedMessagesService = localizedMessagesService;
    }

    @Override
    public List<Question> findAll() {
        String fileName = fileNameProvider.getTestFileName();

        if (fileName == null) {
            String message = localizedMessagesService.getMessage("TestService.error.file.null", "");
            throw new IllegalArgumentException(message);
        }

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                String message = localizedMessagesService.getMessage("TestService.error.file.not.found", "");
                throw new QuestionReadException(message);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                List<QuestionDto> questionDtos = parseFileQuestions(reader);
                List<Question> domainObjects = convertQuestionToDomain(questionDtos);
                return domainObjects;
            }
        } catch (IOException e) {
            String message = localizedMessagesService.getMessage("TestService.error.read.file","");
            throw new QuestionReadException(message + e.getMessage(), e);
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
