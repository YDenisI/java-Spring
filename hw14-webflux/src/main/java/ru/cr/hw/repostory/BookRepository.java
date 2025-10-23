package ru.cr.hw.repostory;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.cr.hw.domain.Book;


public interface BookRepository extends ReactiveMongoRepository<Book, String> {

}
