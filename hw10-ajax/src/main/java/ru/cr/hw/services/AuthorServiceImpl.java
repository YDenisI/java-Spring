package ru.cr.hw.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cr.hw.domain.Author;
import ru.cr.hw.dto.AuthorDto;
import ru.cr.hw.repostory.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(AuthorDto::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDto findById(Long id) {
        Author author = authorRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(id)));
        return AuthorDto.fromDomain(author);
    }
}
