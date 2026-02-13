package ru.cr.hw.repostory;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cr.hw.domain.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
