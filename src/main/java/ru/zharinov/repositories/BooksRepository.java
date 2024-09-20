package ru.zharinov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zharinov.entities.Book;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
}
