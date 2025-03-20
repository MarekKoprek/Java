package com.example.second;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class HolidayController {
    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/holiday")
    public String holiday(@RequestParam String country, Model model) {
        WebClient webClient = WebClient.create();
        String url = "https://date.nager.at/api/v3/nextpublicholidays/";

        if (country.equals("PL") || country.equals("DE")) {
            url += country;
            String response = webClient.get().uri(url).retrieve().bodyToMono(String.class).block();

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                List<Holiday> holidays = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, Holiday.class));
                model.addAttribute("holidays", holidays);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            model.addAttribute("error", "Wrong country");
        }
        return "holiday";
    }
}
