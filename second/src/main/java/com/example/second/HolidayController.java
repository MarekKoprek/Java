package com.example.second;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HolidayController {
    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    private int countFreeDays(LocalDate date, List<LocalDate> holidays, int length) {
        int count = 0;
        for (int i = 0; i < length; i++) {
            if(date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    date.getDayOfWeek() == DayOfWeek.SUNDAY ||
                    holidays.contains(date)) {
                count++;
            }
            date = date.plusDays(1);
        }
        return count;
    }

    private LocalDate findDeadline(LocalDate date, List<LocalDate> holidays, int length) {
        for (int i = 0; i < length; i++) {
            if(date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    date.getDayOfWeek() == DayOfWeek.SUNDAY ||
                    holidays.contains(date)) {
                i--;
            }
            date = date.plusDays(1);
        }
        return date;
    }

    @GetMapping("/holiday")
    public String holiday(@RequestParam String country,
                          @RequestParam(required = false) Integer month,
                          @RequestParam(required = false) Integer year,
                          @RequestParam(required = false) Integer days,
                          Model model) {
        WebClient webClient = WebClient.create();
        String url = "https://date.nager.at/api/v3/nextpublicholidays/";

        if (country.equals("PL") || country.equals("DE")) {
            url += country;
            String response = webClient.get().uri(url).retrieve().bodyToMono(String.class).block();

            ObjectMapper objectMapper = new ObjectMapper();
            List<LocalDate> dates = new ArrayList<>();
            try {
                List<Holiday> holidays = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, Holiday.class));
                for(Holiday holiday : holidays) {
                    dates.add(LocalDate.parse(holiday.getDate()));
                }

                if(dates.contains(LocalDate.now())) {
                    model.addAttribute("today", "Dzisiaj jest święto");
                }
                else {
                    model.addAttribute("today", "Dzisiaj nie ma święta");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            int freeDays = 0;
            if(month != null) {
                LocalDate chosenDate = LocalDate.of(year, month, 1);
                freeDays = countFreeDays(chosenDate, dates, chosenDate.lengthOfMonth());
            }
            else if(year != null) {
                LocalDate chosenDate = LocalDate.of(year, 1, 1);
                freeDays = countFreeDays(chosenDate, dates, chosenDate.lengthOfYear());
            }
            model.addAttribute("freeDays", freeDays);

            if(days != null) {
                model.addAttribute("deadline", findDeadline(LocalDate.now(), dates, days));
            }

            model.addAttribute("country", country);
        }
        else{
            model.addAttribute("error", "Wrong country");
        }
        return "holiday";
    }
}
