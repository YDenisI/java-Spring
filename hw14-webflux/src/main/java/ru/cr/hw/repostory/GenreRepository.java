package ru.cr.hw.repostory;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.cr.hw.domain.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {

}
