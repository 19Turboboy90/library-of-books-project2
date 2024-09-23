package ru.zharinov.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zharinov.entities.Person;
import ru.zharinov.services.PeopleService;
import ru.zharinov.util.PersonValidator;

@Controller
@RequestMapping("/people")
@RequiredArgsConstructor
public class PeopleController {
    private final PeopleService peopleService;
    private final PersonValidator personValidator;

    @GetMapping()
    public String showPeople(Model model) {
        model.addAttribute("people", peopleService.getAllPeople());
        return "people/show-people";
    }

    @GetMapping("/new")
    public String newPeople(@ModelAttribute("person") Person person) {
        return "people/new-person";
    }

    @PostMapping()
    public String savePerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/new-person";
        }
        peopleService.savePerson(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String infoPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.getPersonById(id));
        return "people/show-person";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        peopleService.deletePersonById(id);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.getPersonById(id));
        return "people/edit-person";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "people/edit-person";
        }
        peopleService.updatePersonById(id, person);
        return "redirect:/people";
    }
}
