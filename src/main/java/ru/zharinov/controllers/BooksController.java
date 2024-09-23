package ru.zharinov.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zharinov.entities.Book;
import ru.zharinov.entities.Person;
import ru.zharinov.services.BooksService;
import ru.zharinov.services.PeopleService;
import ru.zharinov.util.BookValidate;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;
    private final BookValidate bookValidate;

    @GetMapping()
    public String showAllBooks(@RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "books_per_page", defaultValue = "4", required = false)
                               Integer booksPerPage,
                               @RequestParam(value = "sort_by_year", defaultValue = "false", required = false)
                               Boolean isSortByYear,
                               Model model) {
        model.addAttribute("books", booksService.getAllBooks(page - 1, booksPerPage, isSortByYear));
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
        booksService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String infoBook(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("people", peopleService.getAllPeople());
        model.addAttribute("book", booksService.getBookById(id));
        return "books/show-book";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.getBookById(id));
        return "books/edit-book";
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/edit-book";
        }
        booksService.updateBookById(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        booksService.deleteBookById(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/add-person")
    public String addPerson(@PathVariable("id") int bookId, @ModelAttribute("person") Person person) {
        booksService.addPersonToBook(bookId, person.getId());
        return "redirect:/books/" + bookId;
    }

    @PatchMapping("/{id}/remove-person")
    public String removePerson(@PathVariable("id") int id) {
        booksService.removePersonByBookId(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchPage() {
        return "books/search";
    }

    @PostMapping("/search")
    public String searchBook(@RequestParam("paramSearch") String paramSearch, Model model) {
        model.addAttribute("books", booksService.getBooksByTitle(paramSearch));
        return "books/search";
    }
}
