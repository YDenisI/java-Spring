package ru.cr.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cr.hw.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
