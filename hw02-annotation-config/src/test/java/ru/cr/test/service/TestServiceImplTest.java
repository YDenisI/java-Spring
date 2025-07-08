package ru.cr.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cr.hw.dao.QuestionDao;
import ru.cr.hw.domain.Answer;
import ru.cr.hw.domain.Question;
import ru.cr.hw.domain.Student;
import ru.cr.hw.domain.TestResult;
import ru.cr.hw.service.IOService;
import ru.cr.hw.service.TestService;
import ru.cr.hw.service.TestServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TestServiceImplTest {
    private IOService ioService;
    private QuestionDao questionDao;
    private TestService testService;
    private Student student;

    @BeforeEach
    public void setUp() {
        ioService = mock(IOService.class);
        questionDao = mock(QuestionDao.class);
        student = new Student("Yakovlev","Denis");

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
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    public void testAllCorrectAnswers() {
        when(ioService.readString()).thenReturn("2").thenReturn("1");
        TestResult result = testService.executeTestFor(student);
        assertNotNull(result);
        assertEquals(student, result.getStudent());
        List<Question> questions = questionDao.findAll();
        assertEquals(questions.size(), result.getRightAnswersCount());
    }
    @Test
    public void testNoQuestions() {
        when(questionDao.findAll()).thenReturn(List.of());
        List<Question> questions = questionDao.findAll();
        var result = testService.executeTestFor(student);
        assertNotNull(result);

        assertEquals(0, result.getRightAnswersCount());
        assertEquals(questions, result.getAnsweredQuestions());

        verify(ioService, never()).readString();


    }
}