package ru.zharinov.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zharinov.dao.BookDao;
import ru.zharinov.dao.PersonDao;
import ru.zharinov.models.Book;
import ru.zharinov.models.Person;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {
    private final BookDao bookDao;
    private final PersonDao personDao;

    @GetMapping
    public String showAllBooks(Model model) {
        model.addAttribute("books", bookDao.getAllBooks());
        return "books/show-books";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new-book";
    }

    @PostMapping()
    public String saveBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new-book";
        }
        bookDao.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String infoBook(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("people", personDao.getAllPeople());
        model.addAttribute("book", bookDao.getBookById(id).get());
        return "books/show-book";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookDao.getBookById(id).get());
        return "books/edit-book";
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/edit-book";
        }
        bookDao.updateBookById(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookDao.deleteBookById(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/add-person")
    public String addPerson(@PathVariable("id") int bookId, @ModelAttribute("person") Person person) {
        System.out.println(bookId);
        System.out.println(person.getId());
        bookDao.addPersonToBook(bookId, person.getId());
        return "redirect:/books/" + bookId;
    }

    @PatchMapping("/{id}/remove-person")
    public String removePerson(@PathVariable("id") int id) {
        bookDao.removePersonByBookId(id);
        return "redirect:/books/" + id;
    }
}
