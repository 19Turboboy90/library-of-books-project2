package ru.zharinov.entities;

import jakarta.persistence.*;
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
@Entity
@Table(name = "person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "The full name should not be empty\n ФИО не должно быть пустым")
    @Size(min = 2, max = 100, message = "The full name should be between 2 and 100 characters\n" +
            "Имя должно быть от 2 до 100 символов длиной")
    @Pattern(regexp = "[A-ZА-ЯЁ][a-zа-яё]+ [A-ZА-ЯЁ][a-zа-яё]+ [A-ZА-ЯЁ][a-zа-яё]+",
            message = "The input format should be as follows - 'Ivanov Ivan Ivanovich'")
    @Column(name = "full_name")
    private String fullName;

    @Min(value = 1900, message = "The year should be longer than 1990\n Год должен быть больше, чем 1990")
    @Column(name = "year_of_birth")
    private int yearOfBirth;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<Book> bookList;
}
