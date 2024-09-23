package ru.zharinov.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zharinov.entities.Book;
import ru.zharinov.entities.Person;
import ru.zharinov.exceptions.MyException;
import ru.zharinov.repositories.BooksRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleService peopleService;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleService peopleService) {
        this.booksRepository = booksRepository;
        this.peopleService = peopleService;
    }

    public List<Book> getAllBooks(int page, int itemsPerPage, boolean isSort) {
        return isSort ? booksRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by("year"))).getContent()
                : booksRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
    }

    public Book getBookById(int id) {
        return booksRepository.findById(id).orElseThrow(() -> new MyException("The Book with " + id + " is not found"));
    }

    @Transactional
    public void saveBook(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void updateBookById(int id, Book book) {
        Book bookById = getBookById(id);
        book.setId(id);
        book.setPerson(bookById.getPerson());
        booksRepository.save(book);
    }

    @Transactional
    public void deleteBookById(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void addPersonToBook(int bookId, int personId) {
        Person person = peopleService.getPersonById(personId);
        Book book = getBookById(bookId);
        book.setDateTimeAddedBook(LocalDateTime.now());
        book.setPerson(person);
        person.getBookList().add(book);

    }

    @Transactional
    public void removePersonByBookId(int bookId) {
        Book book = getBookById(bookId);
        Person person = book.getPerson();
        book.setPerson(null);
        book.setDateTimeAddedBook(null);
        book.setExpired(false);
        person.getBookList().remove(book);
    }

    public List<Book> getBooksByTitle(String paramSearch) {
        return booksRepository.findBookByTitleStartingWith(paramSearch);
    }
}
