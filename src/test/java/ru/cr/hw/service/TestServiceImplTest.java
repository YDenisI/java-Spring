package ru.cr.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cr.hw.dao.QuestionDao;
import ru.cr.hw.domain.Answer;
import ru.cr.hw.domain.Question;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TestServiceImplTest {
    private IOService ioService;
    private QuestionDao questionDao;
    private TestService testService;

    @BeforeEach
    public void setUp() {
        ioService = mock(IOService.class);
        questionDao = mock(QuestionDao.class);

        List<Question> questions = Arrays.asList(
                new Question("Question 1", Arrays.asList(
                        new Answer("Answer 1", false),
                        new Answer("Answer 2", true)
                )),
                new Question("Question 2", Arrays.asList(
                        new Answer("Answer A", false),
                        new Answer("Answer B", true)
                ))
        );

        when(questionDao.findAll()).thenReturn(questions);
        testService = new TestServiceImpl(questionDao,ioService);
    }

    @Test
    public void testAllCorrectAnswers() {
        when(ioService.readLine()).thenReturn("2").thenReturn("2");
        testService.executeTest();
        verify(ioService).printFormattedLine(contains("Answer correct:"), eq(2), eq(2));
    }
    @Test
    public void testNoQuestions() {
        when(questionDao.findAll()).thenReturn(List.of());

        testService.executeTest();

        verify(ioService).printFormattedLine(contains("Answer correct:"), eq(0), eq(0));
    }
}