package ru.cr.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cr.hw.controller.NotFoundException;
import ru.cr.hw.domain.Genre;
import ru.cr.hw.dto.GenreDto;
import ru.cr.hw.repostory.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(GenreDto::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    public GenreDto findById(Long id) {
        Genre genre = genreRepository.findById(id).
                orElseThrow(() -> new NotFoundException());
        return GenreDto.fromDomain(genre);
    }
}
