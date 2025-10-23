package ru.cr.hw.repostory;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.cr.hw.domain.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {

}
