package ru.zharinov.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.zharinov.entities.Book;
import ru.zharinov.services.BooksService;

@Component
public class BookValidate implements Validator {
    private final BooksService booksService;

    @Autowired
    public BookValidate(BooksService booksService) {
        this.booksService = booksService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String string = (String) target;
        if (booksService.getBookByTitle(string).isEmpty()) {
            errors.rejectValue("paramSearch", "", "It shouldn't be empty");
        }
    }
}
