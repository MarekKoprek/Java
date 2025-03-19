package com.example.first;

import com.example.first.model.Person;
import com.example.first.model.Voivodeship;
import com.example.first.repo.PersonRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PersonController {
    @Autowired
    PersonRepo personRepo;

    @GetMapping("/addPerson")
    public String showForm(Model model) {
        model.addAttribute("person", new Person());
        model.addAttribute("voivodeships", Voivodeship.values());
        return "addPersonForm";
    }

    @PostMapping("/addPerson")
    public String addPerson(@Valid @ModelAttribute("person") Person person, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("voivodeships", Voivodeship.values());
            return "addPersonForm";
        }

        personRepo.save(person);
        return "redirect:/success";
    }
}
