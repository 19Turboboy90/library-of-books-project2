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
public class BookDao {
    private final JdbcTemplate template;

    @Autowired
    public BookDao(JdbcTemplate template) {
        this.template = template;
    }

    private static final String GET_ALL_BOOKS = """
            SELECT id, title, author, year
            FROM book;
            """;

    private static final String GET_BOOK_BY_ID = """
            SELECT id, title, author, year
            FROM book
            WHERE id=?;
            """;

    private static final String GET_PERSON_BY_BOOK_ID = """
            SELECT p.id,
                   p.full_name,
                   p.year_of_birth
            FROM person p
                JOIN book b ON p.id = b.person_id
            WHERE b.id = ?;
            """;

    private static final String SAVE_BOOK = """
            INSERT INTO book (title, author, year) VALUES (?,?,?)
            """;

    private static final String UPDATE_BOOK_BY_ID = """
            UPDATE book
            SET title = ?, author = ?, year=?
            WHERE id=?
            """;

    private static final String ADD_PERSON_TO_BOOK = """
            UPDATE book
            SET  person_id = ?
            WHERE id = ?;
            """;

    private static final String REMOVE_PERSON_TO_BOOK = """
            UPDATE book
            SET  person_id = ?
            WHERE id = ?;
            """;

    private static final String DELETE_BOOK_BY_ID = """
            DELETE FROM book
            WHERE id = ?;
            """;

    public List<Book> getAllBooks() {
        return template.query(GET_ALL_BOOKS, new BeanPropertyRowMapper<>(Book.class));
    }

    public Optional<Book> getBookById(int id) {
        var book = template.query(GET_BOOK_BY_ID, new BeanPropertyRowMapper<>(Book.class), id)
                .stream().filter(b -> b.getId() == id).findFirst();
        var personByBookId = getPersonByBookId(id);
        if (personByBookId != null) {
            book.get().setPerson(personByBookId);
            return book;
        }
        return book;
    }

    //Получение Person через join по внешнему ключу book_personId
    private Person getPersonByBookId(int id) {
        return template.query(GET_PERSON_BY_BOOK_ID, new BeanPropertyRowMapper<>(Person.class), id)
                .stream().findFirst().orElse(null);
    }

    public void saveBook(Book book) {
        template.update(SAVE_BOOK, book.getTitle(), book.getAuthor(), book.getYear());
    }

    public void updateBookById(int id, Book book) {
        template.update(UPDATE_BOOK_BY_ID, book.getTitle(), book.getAuthor(), book.getYear(), id);
    }

    //Добавление Person в DB в books
    public void addPersonToBook(int bookId, int personId) {
        template.update(ADD_PERSON_TO_BOOK, personId, bookId);
    }

    public void deleteBookById(int id) {
        template.update(DELETE_BOOK_BY_ID, id);
    }

    //Удаление Person из DB books
    public void removePersonByBookId(int id) {
        template.update(REMOVE_PERSON_TO_BOOK, null, id);
    }

}
