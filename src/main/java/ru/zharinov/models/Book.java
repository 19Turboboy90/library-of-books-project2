package ru.zharinov.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Book {
    private int id;
    @NotEmpty(message = "The title should not be empty\n Название книги не должно быть пустым")
    @Size(min = 2, max = 100, message = "The title of the book should be from 2 to 100 characters long\n" +
                                        "Название книги должно быть от 2 до 100 символов длиной")
    private String title;
    @NotEmpty(message = "The author should not be empty\n Автор книги не должно быть пустым")
    @Size(min = 2, max = 100, message = "The name of the author of the book should be from 2 to 100 characters long\n" +
                                        "Имя автора книги должно быть от 2 до 100 символов длиной")
    private String author;
    @Min(value = 1500, message = "The year should be more than 1500\n Год должен быть больше, чеи 1500")
    private int year;
    private Person person;
}
