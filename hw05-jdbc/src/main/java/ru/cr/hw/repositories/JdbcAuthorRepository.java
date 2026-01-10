package ru.cr.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.cr.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private static final String SQL_QUERY_AUTHOR_FIND_ALL = "select * from authors";

    private static final String SQL_QUERY_AUTHOR_FIND_BY_ID = "select * from authors where id = :id";

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public JdbcAuthorRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public List<Author> findAll() {
        return namedParameterJdbcOperations.query(
                SQL_QUERY_AUTHOR_FIND_ALL,
                Collections.emptyMap(),
                new AuthorRowMapper()
        );
    }

    @Override
    public Optional<Author> findById(long id) {

        Map<String, Object> params = Collections.singletonMap("id", id);

        List<Author> authors = namedParameterJdbcOperations.query(SQL_QUERY_AUTHOR_FIND_BY_ID,
                params, new AuthorRowMapper());
        if (authors.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(authors.get(0));
        }
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("full_name");
            return new Author(id, name);
        }
    }
}
