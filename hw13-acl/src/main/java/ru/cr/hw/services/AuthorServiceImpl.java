package ru.cr.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cr.hw.domain.Author;
import ru.cr.hw.dto.AuthorDto;
import ru.cr.hw.repostory.AuthorRepository;
import ru.cr.hw.rest.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public List<AuthorDto> findAll() {
        List<AuthorDto> authors = authorRepository.findAll().stream()
                .map(AuthorDto::fromDomain)
                .collect(Collectors.toList());
        if (authors.isEmpty()) {
            throw new NotFoundException("Authors not found!");
        }
        return authors;
    }

    @Override
    public AuthorDto findById(Long id) {
        Author author = authorRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Author not found!"));
        return AuthorDto.fromDomain(author);
    }
}
