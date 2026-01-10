import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cr.hw.config.TestFileNameProvider;
import ru.cr.hw.dao.CsvQuestionDao;
import ru.cr.hw.domain.Answer;
import ru.cr.hw.domain.Question;
import ru.cr.hw.exceptions.QuestionReadException;
import ru.cr.hw.service.LocalizedMessagesService;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(
        classes = {/*Application.class, */CsvQuestionDaoTest.TestMocksConfig.class},
        properties = {
                "spring.shell.interactive.enabled=false",
                "spring.shell.command.script.enabled=false",
                "spring.shell.command.quit.enabled=false",
                "spring.shell.command.clear.enabled=false"
        }
)
class CsvQuestionDaoTest {

    @Configuration
    static class TestMocksConfig {

        @Bean(name = "validFileNameProvider")
        public TestFileNameProvider validFileNameProvider() {
            return () -> "questions.csv";
        }

        @Bean(name = "missingFileNameProvider")
        public TestFileNameProvider missingFileNameProvider() {
            return () -> "nonexistent_file.csv";
        }

        @Bean
        public CsvQuestionDao csvQuestionDaoWithValidFileName(@Qualifier("validFileNameProvider") TestFileNameProvider provider,
                                                              LocalizedMessagesService localizedMessagesService) {
            return new CsvQuestionDao(provider, localizedMessagesService);
        }

        @Bean
        public CsvQuestionDao csvQuestionDaoWithMissingFileName(@Qualifier("missingFileNameProvider") TestFileNameProvider provider,
                                                                LocalizedMessagesService localizedMessagesService) {
            return new CsvQuestionDao(provider, localizedMessagesService);
        }

        @Bean
        public LocalizedMessagesService localizedMessagesService() {
            return Mockito.mock(LocalizedMessagesService.class);
        }
    }

    @Autowired
    private CsvQuestionDao csvQuestionDaoWithValidFileName;

    @Autowired
    private CsvQuestionDao csvQuestionDaoWithMissingFileName;

    @Autowired
    private LocalizedMessagesService localizedMessagesService;

    @Test
    public void testFindAll_ShouldParseQuestionsCorrectly() {

        List<Question> questions = csvQuestionDaoWithValidFileName.findAll();

        assertNotNull(questions);
        assertEquals(3, questions.size());

        Question q1 = questions.get(0);
        assertEquals("Is there life on Mars?", q1.text());

        List<Answer> answers = q1.answers();

        Answer correctAnswer = answers.get(0);
        assertEquals("Science doesn't know this yet", correctAnswer.text());
        assertTrue(correctAnswer.isCorrect());

        Answer answer2 = answers.get(1);
        assertEquals("Certainly. The red UFO is from Mars. And green is from Venus", answer2.text());
        assertFalse(answer2.isCorrect());

        Answer answer3 = answers.get(2);
        assertEquals("Absolutely not", answer3.text());
        assertFalse(answer3.isCorrect());
    }

    @Test
    public void findAll_ShouldThrowQuestionReadException_WhenFileNotFound() {

        when(localizedMessagesService.getMessage(eq("TestService.error.file.not.found"), any()))
                .thenReturn("File not found");

        QuestionReadException thrown = assertThrows(
                QuestionReadException.class,
                () -> csvQuestionDaoWithMissingFileName.findAll(),
                "Expected findAll() to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("File not found"));
    }

    @Test
    public void testFindAll_ShouldSkipCommentLines() {

        List<Question> questions = csvQuestionDaoWithValidFileName.findAll();
        for (Question question : questions) {
            assertFalse(question.text().startsWith("#"));
        }
    }
}