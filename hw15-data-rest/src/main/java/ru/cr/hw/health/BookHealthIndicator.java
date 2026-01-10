package ru.cr.hw.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.cr.hw.repostory.BookRepository;

@Component
public class BookHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;

    public BookHealthIndicator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Health health() {
        try {
            long bookCount = bookRepository.count();
            if (bookCount >= 0) {
                return Health.up()
                        .withDetail("bookCount", bookCount)
                        .withDetail("status", "Database connection is healthy")
                        .build();
            } else {
                return Health.down()
                        .withDetail("error", "Unexpected book count")
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", "Database connection failed: " + e.getMessage())
                    .build();
        }
    }
}