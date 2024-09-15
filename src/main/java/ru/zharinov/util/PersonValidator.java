package ru.zharinov.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.zharinov.dao.PersonDao;
import ru.zharinov.models.Person;

@Component
public class PersonValidator implements Validator {
    private final PersonDao personDao;

    @Autowired
    public PersonValidator(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if (personDao.getPersonByFullName(person.getFullName()).isPresent()) {
            errors.rejectValue("fullName", "", "A person with such a full name already exists" +
                                               "\nЧеловек с такой ФИО уже существует");
        }
    }
}
