package ru.cr.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cr.hw.dao.QuestionDao;
import ru.cr.hw.domain.Answer;
import ru.cr.hw.domain.Student;
import ru.cr.hw.domain.TestResult;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
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
            int userChoice = readUserChoice(answers.size());
            Answer selectedAnswer = answers.get(userChoice - 1);
            if (selectedAnswer.isCorrect()) {
                isAnswerValid = true;
            }
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private int readUserChoice(int numberOfAnswers) {
        int choice = -1;
        while (true) {
            ioService.printLine("Enter the answer number:");
            String input = ioService.readString();
            try {
                choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= numberOfAnswers) {
                    break;
                } else {
                    ioService.printLine("Please enter a number from 1 to " + numberOfAnswers);
                }
            } catch (NumberFormatException e) {
                ioService.printLine("Incorrect input. Try again.");
            }
        }
        return choice;
    }
}
