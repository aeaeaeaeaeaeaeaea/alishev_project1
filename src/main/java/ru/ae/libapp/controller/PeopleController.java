package ru.ae.libapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ae.libapp.dao.PersonDao;
import ru.ae.libapp.model.Book;
import ru.ae.libapp.model.Person;
import ru.ae.libapp.services.PeopleService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    //Метод для отображения всех людей из БД
    @GetMapping()
    public String personList(Model model, @RequestParam(value = "page", required = false) String page,
                             @RequestParam(value = "itemsPerPage", required = false) String itemsPerPage,
                             @RequestParam(value = "sort_by_year", required = false) String sort_by_year) {

        if (page == null && itemsPerPage == null && sort_by_year == null) {
            model.addAttribute("person", peopleService.findAll());
            return "person/people";
        }

        if (sort_by_year != null && Boolean.parseBoolean(sort_by_year) == true && page == null && itemsPerPage == null) {
            model.addAttribute("person", peopleService.findAll("year"));
            return "person/people";
        }

        if ((sort_by_year != null && Boolean.parseBoolean(sort_by_year) == true) && (page != null && itemsPerPage != null)) {
            model.addAttribute("person", peopleService.findAll(Integer.parseInt(page), Integer.parseInt(itemsPerPage), "year"));
            return "person/people";
        }

        model.addAttribute("person", peopleService.findAll(Integer.parseInt(page), Integer.parseInt(itemsPerPage)));

        return "person/people";
    }

    //Метод для отображения конкретного человека по его ID
    @GetMapping("/{id}")
    public String personPage(@PathVariable("id") int id, Model model) {
        //Метод getPersonById возвращает человек из БД по его ID
        model.addAttribute("personPage", peopleService.findById(id));
        model.addAttribute("personList", peopleService.getPersonsBooksById(id));
        return "person/personPage";
    }

    //Метод для редактирования существующего человека по его ID
    @GetMapping("/{id}/edit")
    public String personEdit(@PathVariable("id") int id, Model model) {
        model.addAttribute("personEdit", peopleService.findById(id));
        return "person/edit";
    }

    //Метод, который принимает данные со страницы '/{id}/edit' и заменяет ими существующие данные
    @PatchMapping("/{id}")
    public String patchMethod(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        peopleService.update(id, person);
        return "redirect:/people";
    }

    //Метод для создания нового человека
    @GetMapping("/new")
    public String personNew(Model model) {
        model.addAttribute("personNew", new Person());
        return "person/new";
    }

    //Метод, который принимает данные со страницы '/new' и создает нового человека
    @PostMapping()
    public String postMethod(@ModelAttribute("person") Person person) {
        peopleService.add(person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deleteMethod(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }


}
