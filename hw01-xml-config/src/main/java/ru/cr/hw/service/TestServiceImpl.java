package ru.cr.hw.service;

import lombok.RequiredArgsConstructor;
import ru.cr.hw.dao.QuestionDao;
import ru.cr.hw.domain.Answer;
import ru.cr.hw.domain.Question;

import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final QuestionDao questionDao;

    private final IOService ioService;

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void executeTest() {
        List<Question> questions = questionDao.findAll();
        int correctAnswersCount = 0;
        for (Question question : questions) {
            ioService.printLine("");
            ioService.printLine(question.text());

            List<Answer> answers = question.answers();
            for (int i = 0; i < answers.size(); i++) {
                ioService.printFormattedLine("%d. %s", i + 1, answers.get(i).text());
            }
            int userChoice = readUserChoice(answers.size());
            Answer selectedAnswer = answers.get(userChoice - 1);
            if (selectedAnswer.isCorrect()) {
                ioService.printLine("Correct!");
                correctAnswersCount++;
            } else {
                ioService.printLine("Incorrect.");
            }
        }
        ioService.printFormattedLine("Test finish! Answer correct: %d из %d.", correctAnswersCount, questions.size());
    }

    private int readUserChoice(int numberOfAnswers) {
        int choice = -1;
        while (true) {
            ioService.printLine("Enter the answer number:");
            String input = ioService.readLine();
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
