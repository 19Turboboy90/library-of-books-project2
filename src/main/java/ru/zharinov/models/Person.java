package ru.zharinov.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Person {
    private int id;

    @NotEmpty(message = "The full name should not be empty\n ФИО не должно быть пустым")
    @Size(min = 2, max = 100, message = "The full name should be between 2 and 100 characters\n" +
                                        "Имя должно быть от 2 до 100 символов длиной")
    @Pattern(regexp = "[A-Z]\\w+ [A-Z]\\w+ [A-Z]\\w+",
            message = "The input format should be as follows - 'Ivanov Ivan Ivanovich'")
    private String fullName;

    @Min(value = 1900, message = "The year should be longer than 1990\n Год должен быть больше, чем 1990")
    private int yearOfBirth;
    private List<Book> bookList;
}
