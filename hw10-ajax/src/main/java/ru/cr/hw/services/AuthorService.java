package ru.cr.hw.services;


import ru.cr.hw.dto.AuthorDto;
import java.util.List;

public interface AuthorService {

    List<AuthorDto> findAll();

    AuthorDto findById(Long id);
}
