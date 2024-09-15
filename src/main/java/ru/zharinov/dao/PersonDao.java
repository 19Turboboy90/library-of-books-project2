package ru.zharinov.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.zharinov.models.Book;
import ru.zharinov.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDao {
    private final JdbcTemplate template;

    @Autowired
    public PersonDao(JdbcTemplate template) {
        this.template = template;
    }

    private static final String GET_ALL_PEOPLE = """
            SELECT id, full_name, year_of_birth
            FROM person;
            """;

    private static final String GET_PERSON_BY_ID = """
            SELECT id, full_name, year_of_birth
            FROM person
            WHERE id = ?;
            """;

    private static final String GET_ALL_BOOKS_BY_PERSON_ID = """
            SELECT b.id,
                   b.title,
                   b.author,
                   b.year
            FROM book b
            JOIN person p ON b.person_id = p.id
            WHERE p.id = ?;
                        
            """;

    private static final String GET_PERSON_BY_FULLNAME = """
            SELECT id, full_name, year_of_birth
            FROM person
            WHERE full_name = ?;
            """;
    private static final String SAVE_PERSON = """
            INSERT INTO person (full_name, year_of_birth) VALUES (?, ?);
            """;

    private static final String UPDATE_PERSON_BY_ID = """
            UPDATE person p
            SET full_name = ?, year_of_birth =?
            WHERE id = ?;
            """;

    private static final String DELETE_PERSON = """
            DELETE FROM person
            WHERE id = ?;
            """;

    public List<Person> getAllPeople() {
        return template.query(GET_ALL_PEOPLE, new BeanPropertyRowMapper<>(Person.class));
    }

    public Optional<Person> getPersonByFullName(String fullname) {
        return template.query(GET_PERSON_BY_FULLNAME, new BeanPropertyRowMapper<>(Person.class), fullname)
                .stream().filter(p -> p.getFullName().equals(fullname)).findAny();
    }

    public Optional<Person> getPersonById(int id) {
        var books = template.query(GET_ALL_BOOKS_BY_PERSON_ID, new BeanPropertyRowMapper<>(Book.class), id);

        var person = template.query(GET_PERSON_BY_ID, new BeanPropertyRowMapper<>(Person.class), id)
                .stream().filter(p -> p.getId() == id).findFirst();
        if (!books.isEmpty() && person.isPresent()) {
            person.get().setBookList(books);
        }
        return person;
    }

    public void savePerson(Person person) {
        template.update(SAVE_PERSON, person.getFullName(), person.getYearOfBirth());
    }

    public void updatePersonById(int id, Person person) {
        template.update(UPDATE_PERSON_BY_ID, person.getFullName(), person.getYearOfBirth(), id);
    }

    public void deletePersonById(int id) {
        template.update(DELETE_PERSON, id);
    }
}
