package ru.cr.hw.service;

import ru.cr.hw.domain.Student;
import ru.cr.hw.domain.TestResult;

public interface TestService {
    TestResult executeTestFor(Student student);
}
