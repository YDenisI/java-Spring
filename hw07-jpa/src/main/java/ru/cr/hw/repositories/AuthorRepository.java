package ru.cr.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cr.hw.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
