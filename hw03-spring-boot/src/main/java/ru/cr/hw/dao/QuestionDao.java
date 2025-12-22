package ru.cr.hw.dao;

import ru.cr.hw.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
}
