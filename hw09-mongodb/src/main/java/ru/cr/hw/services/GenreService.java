package ru.cr.hw.services;

import ru.cr.hw.dto.GenreDto;

import java.util.List;

public interface GenreService {

    List<GenreDto> findAll();

    GenreDto findById(String id);

}
