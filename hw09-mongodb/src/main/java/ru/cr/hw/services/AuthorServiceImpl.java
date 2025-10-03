package ru.cr.hw.services;


import org.springframework.stereotype.Service;
import ru.cr.hw.dto.AuthorDto;
import ru.cr.hw.exceptions.EntityNotFoundException;
import ru.cr.hw.models.Author;
import ru.cr.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<AuthorDto> findAll() {
        List<AuthorDto> authors = authorRepository.findAll().stream()
                .map(AuthorDto::fromDomain)
                .collect(Collectors.toList());
        if (authors.isEmpty()) {
            throw new EntityNotFoundException("Authors not found");
        }
        return authors;
    }

    @Override
    public AuthorDto findById(String id) {
        Author author = authorRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Author not found"));
        return AuthorDto.fromDomain(author);
    }
}
