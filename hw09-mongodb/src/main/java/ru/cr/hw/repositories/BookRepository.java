package ru.cr.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.cr.hw.models.Book;


public interface BookRepository extends MongoRepository<Book, String> {

}
