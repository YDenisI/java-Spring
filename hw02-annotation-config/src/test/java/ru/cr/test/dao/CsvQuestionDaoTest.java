package ru.cr.test.dao;

import org.junit.jupiter.api.Test;
import ru.cr.hw.config.TestFileNameProvider;
import ru.cr.hw.dao.CsvQuestionDao;
import ru.cr.hw.domain.Answer;
import ru.cr.hw.domain.Question;
import ru.cr.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvQuestionDaoTest {
    private static class TestFileNameProviderMock implements TestFileNameProvider {
        @Override
        public String getTestFileName() {
            return "questions.csv";
        }
    }

    private static class MissingFileNameProvider implements TestFileNameProvider {
        @Override
        public String getTestFileName() {
            return "nonexistent_file.csv";
        }
    }

    @Test
    public void testFindAll_ShouldParseQuestionsCorrectly() {
        CsvQuestionDao dao = new CsvQuestionDao(new TestFileNameProviderMock());

        List<Question> questions = dao.findAll();

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
        CsvQuestionDao dao = new CsvQuestionDao(new MissingFileNameProvider());

        QuestionReadException thrown = assertThrows(
                QuestionReadException.class,
                () -> dao.findAll(),
                "Expected findAll() to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("File not found"));
    }

    @Test
    public void testFindAll_ShouldSkipCommentLines() {
        CsvQuestionDao dao = new CsvQuestionDao(new TestFileNameProviderMock());

        List<Question> questions = dao.findAll();
        for (Question question : questions) {
            assertFalse(question.text().startsWith("#"));
        }
    }
}