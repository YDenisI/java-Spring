import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import ru.cr.hw.Application;
import ru.cr.hw.dao.QuestionDao;
import ru.cr.hw.domain.Answer;
import ru.cr.hw.domain.Question;
import ru.cr.hw.domain.Student;
import ru.cr.hw.domain.TestResult;
import ru.cr.hw.service.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(
        classes = {/*Application.class, */TestServiceImplTest.TestMocksConfig.class},
        properties = {
                "spring.shell.interactive.enabled=false",
                "spring.shell.command.script.enabled=false",
                "spring.shell.command.quit.enabled=false",
                "spring.shell.command.clear.enabled=false"
        }
)
class TestServiceImplTest {

    @Configuration
    public static class TestMocksConfig {

        @Bean
        public LocalizedIOService localizedIOService() {
            return Mockito.mock(LocalizedIOService.class);
        }

        @Bean
        public QuestionDao questionDao() {
            return Mockito.mock(QuestionDao.class);
        }

        @Bean
        public TestService testService(@Autowired @Qualifier("localizedIOService") LocalizedIOService ioService, @Autowired QuestionDao questionDao) {
            return new TestServiceImpl(ioService, questionDao);
        }
        @Bean
        public LocalizedMessagesService localizedMessagesService() {
            return Mockito.mock(LocalizedMessagesService.class);
        }
    }

    @Autowired
    @Qualifier("localizedIOService")
    private LocalizedIOService ioService;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private TestServiceImpl testService;

    private final Student student = new Student("Yakovlev", "Denis");

    @Test
    void testAllCorrectAnswers() {
        List<Question> questions = Arrays.asList(
                new Question("Question 1", Arrays.asList(
                        new Answer("Answer 1", false),
                        new Answer("Answer 2", true)
                )),
                new Question("Question 2", Arrays.asList(
                        new Answer("Answer A", true),
                        new Answer("Answer B", false)
                ))
        );

        when(questionDao.findAll()).thenReturn(questions);


        when(ioService.readIntForRangeWithPromptLocalized(
                eq(1),
                eq(2),
                eq("TestService.answer.numbers"),
                eq("TestService.incorrect.input")))
                .thenReturn(2)
                .thenReturn(1);

        TestResult result = testService.executeTestFor(student);

        assertNotNull(result);
        assertEquals(student, result.getStudent());
        assertEquals(questions.size(), result.getRightAnswersCount());
    }

    @Test
    void testNoQuestions() {
        when(questionDao.findAll()).thenReturn(List.of());

        TestResult result = testService.executeTestFor(student);

        assertNotNull(result);
        assertEquals(0, result.getRightAnswersCount());
        assertTrue(result.getAnsweredQuestions().isEmpty());
    }
}