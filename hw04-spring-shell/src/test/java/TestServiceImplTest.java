import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.cr.hw.dao.QuestionDao;
import ru.cr.hw.domain.Answer;
import ru.cr.hw.domain.Question;
import ru.cr.hw.domain.Student;
import ru.cr.hw.domain.TestResult;
import ru.cr.hw.service.LocalizedIOService;
import ru.cr.hw.service.TestServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @Mock
    private LocalizedIOService ioService;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
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