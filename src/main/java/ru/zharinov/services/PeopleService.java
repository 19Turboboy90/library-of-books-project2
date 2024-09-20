package ru.zharinov.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zharinov.entities.Person;
import ru.zharinov.exceptions.MyException;
import ru.zharinov.repositories.PeopleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> getAllPeople() {
        return peopleRepository.findAll();
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepository.findPersonByFullName(fullName);
    }

    public Person getPersonById(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        person.ifPresent(value -> Hibernate.initialize(value.getBookList()));
        person.ifPresent(p -> p.getBookList().forEach(book -> {
            if (book.getDateTimeAddedBook().plusDays(10).isBefore(LocalDateTime.now())) {
                book.setExpired(true);
            }
        }));

        return person.orElseThrow(() -> new MyException("The Person with " + id + " is not found"));
    }

    @Transactional
    public void savePerson(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void updatePersonById(int id, Person person) {
        Person personById = getPersonById(id);
        personById.setFullName(person.getFullName());
        personById.setYearOfBirth(person.getYearOfBirth());
    }

    @Transactional
    public void deletePersonById(int id) {
        peopleRepository.deleteById(id);
    }
}
