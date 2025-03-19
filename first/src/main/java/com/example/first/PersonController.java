package com.example.first;

import com.example.first.model.Gender;
import com.example.first.model.Person;
import com.example.first.model.Voivodeship;
import com.example.first.repo.PersonRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class PersonController {
    @Autowired
    PersonRepo personRepo;

    public static String getPolishVoivodeshipName(Voivodeship voivodeship) {
        switch (voivodeship) {
            case LODZKIE: return "łódzkie";
            case MAZOWIECKIE: return "mazowieckie";
            case MALOPOLSKIE: return "małopolskie";
            case LUBELSKIE: return "lubelskie";
            default: return "";
        }
    }

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
        if (personRepo.existsByPeselNumber(person.getPeselNumber())) {
            model.addAttribute("voivodeships", Voivodeship.values());
            model.addAttribute("error", "PSEEL już zajęty");
            return "addPersonForm";
        }

        int genderNum = person.getPeselNumber().charAt(9) - '0';
        if(genderNum % 2 == 0) {
            person.setGender(Gender.FEMALE);
        }
        else {
            person.setGender(Gender.MALE);
        }

        int year = (person.getPeselNumber().charAt(0) - '0') * 10 + person.getPeselNumber().charAt(1) - '0';
        int month = (person.getPeselNumber().charAt(2) - '0') * 10 + person.getPeselNumber().charAt(3) - '0';
        int day = (person.getPeselNumber().charAt(4) - '0') * 10 + person.getPeselNumber().charAt(5) - '0';
        if(month > 80){
            year += 1800;
            month -= 80;
        }
        else if(month > 60){
            year += 2200;
            month -= 60;
        }
        else if(month > 40){
            year += 2100;
            month -= 40;
        }
        else if(month > 20){
            year += 2000;
            month -= 20;
        }
        else
            year += 1900;

        person.setBirthDate(LocalDate.of(year, month, day));

        personRepo.save(person);
        model.addAttribute("voivodeships", Voivodeship.values());
        model.addAttribute("person", new Person());
        model.addAttribute("success", "Dodano osobę");
        return "addPersonForm";
    }
}
