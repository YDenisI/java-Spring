package ru.cr.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.cr.hw.exceptions.EntityNotFoundException;
import ru.cr.hw.models.Author;
import ru.cr.hw.models.Book;
import ru.cr.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepository {

    private static final String SQL_SELECT_BOOK_FIND_ALL = "select b.id as book_id, b.title as book_title,\n" +
            " a.id as author_id, a.full_name as author_full_name,\n" +
            " g.id as genre_id, g.name as genre_name\n" +
            " from books b\n" +
            " left join authors a on b.author_id = a.id\n" +
            " left join genres g on b.genre_id = g.id\n" +
            " order by b.id";

    private static final String SQL_SELECT_BOOK_FIND_BY_ID = "select b.id as book_id, b.title as book_title,\n" +
            " a.id as author_id, a.full_name as author_full_name,\n" +
            " g.id as genre_id, g.name as genre_name\n" +
            " from books b\n" +
            " left join authors a on b.author_id = a.id\n" +
            " left join genres g on b.genre_id = g.id\n" +
            " where b.id = :id\n"+
            " order by b.id";

    private static final String SQL_INSERT_BOOK = "insert into books(title, author_id, genre_id) " +
                                                            "values (:title, :author_id, :genre_id)";

    private static final String SQL_DELETE_BOOK = "delete from books where id = :id";

    private static final String SQL_UPDATE_BOOK = "update books set title = :title , author_id = :author_id," +
                                                                "genre_id = :genre_id " +
                                                                "where id = :id";

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public JdbcBookRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);

        List<Book> genres = namedParameterJdbcOperations.query(SQL_SELECT_BOOK_FIND_BY_ID, params,
                new BookRowMapper());
        if (genres.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(genres.get(0));
        }
    }

    @Override
    public List<Book> findAll() {
        return namedParameterJdbcOperations.query(
                SQL_SELECT_BOOK_FIND_ALL,
                Collections.emptyMap(),
                new BookRowMapper()
        );
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {

       namedParameterJdbcOperations.update(
                SQL_DELETE_BOOK,
                Map.of(
                        "id", id
                )
        );
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("author_id", book.getAuthor().getId())
                .addValue("genre_id", book.getGenre().getId());

        namedParameterJdbcOperations.update(SQL_INSERT_BOOK, params, keyHolder, new String[]{"id"});

        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {

        int count = namedParameterJdbcOperations.update(
                SQL_UPDATE_BOOK,
                Map.of(
                        "title", book.getTitle(),
                        "author_id", book.getAuthor().getId(),
                        "genre_id", book.getGenre().getId(),
                        "id", book.getId()
                )
        );

        if (count == 0) {
            throw new EntityNotFoundException("No update record");
        }

        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            var author = new Author(
                    rs.getLong("author_id"),
                    rs.getString("author_full_name")
            );
            var genre = new Genre(
                    rs.getLong("genre_id"),
                    rs.getString("genre_name")
            );
            return new Book(id, title, author,genre);
        }
    }
}
