package ru.cr.hw;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.cr.hw.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class TestCommands {

    private final TestRunnerService testRunnerService;

    @ShellMethod(value = "Run-test", key = "RunTest")
    public String runTest() {
        testRunnerService.run();
        return "Test completed";
    }
}
