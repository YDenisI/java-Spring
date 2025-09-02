package ru.cr.hw.services;

import ru.cr.hw.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    List<Genre> findAll();

    Optional<Genre> findById(Long id);
}
