package ru.cr.hw.repostory;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cr.hw.domain.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
