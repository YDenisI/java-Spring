package ru.cr.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cr.hw.dao.QuestionDao;
import ru.cr.hw.domain.Answer;
import ru.cr.hw.domain.Student;
import ru.cr.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
        for (var question: questions) {
            var isAnswerValid = false;
            ioService.printLine("");
            ioService.printLine(question.text());
            List<Answer> answers = question.answers();
            for (int i = 0; i < answers.size(); i++) {
                ioService.printFormattedLine("%d. %s", i + 1, answers.get(i).text());
            }
            int userChoice = ioService.readIntForRangeWithPromptLocalized(1, answers.size(),
                    "TestService.answer.numbers","TestService.incorrect.input");
            Answer selectedAnswer = answers.get(userChoice - 1);
            if (selectedAnswer.isCorrect()) {
                isAnswerValid = true;
            }
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
