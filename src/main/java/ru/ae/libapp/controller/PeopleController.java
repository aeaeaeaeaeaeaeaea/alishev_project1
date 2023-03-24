package ru.ae.libapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ae.libapp.dao.PersonDao;
import ru.ae.libapp.model.Book;
import ru.ae.libapp.model.Person;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDao personDao;

    @Autowired
    public PeopleController(PersonDao personDao) {
        this.personDao = personDao;
    }

    //Метод для отображения всех людей из БД
    @GetMapping()
    public String personList(Model model) {
        //метод show() возвращает список людей из БД
        model.addAttribute("person", personDao.show());
        return "person/people";
    }

    //Метод для отображения конкретного человека по его ID
    @GetMapping("/{id}")
    public String personPage(@PathVariable("id") int id, Model model) {
        //Метод getPersonById возвращает человек из БД по его ID
        model.addAttribute("personPage", personDao.getPersonById(id));
        model.addAttribute("personList", personDao.getListOfPersonBooks(id));
        return "person/personPage";
    }

    //Метод для редактирования существующего человека по его ID
    @GetMapping("/{id}/edit")
    public String personEdit(@PathVariable("id") int id, Model model) {
        model.addAttribute("personEdit", personDao.getPersonById(id));
        return "person/edit";
    }

    //Метод, который принимает данные со страницы '/{id}/edit' и заменяет ими существующие данные
    @PatchMapping("/{id}")
    public String patchMethod(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        personDao.edit(id, person);
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
        personDao.add(person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deleteMethod(@PathVariable("id") int id) {
        personDao.delete(id);
        return "redirect:/people";
    }


}
