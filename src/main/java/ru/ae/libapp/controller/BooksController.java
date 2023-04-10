package ru.ae.libapp.controller;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ae.libapp.model.Book;
import ru.ae.libapp.model.Person;
import ru.ae.libapp.services.BookService;
import ru.ae.libapp.services.PeopleService;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    //Метод для отображения всех книг
    @GetMapping()
    public String bookList(Model model, @RequestParam(value = "page", required = false) String page,
                           @RequestParam(value = "itemsPerPage", required = false) String itemsPerPage) {
        if (page == null && itemsPerPage == null) {
            model.addAttribute("bookList", bookService.findAll());
            return "book/books";
        }

        model.addAttribute("bookList", bookService.findAll(Integer.parseInt(page), Integer.parseInt(itemsPerPage)));

        return "book/books";
    }

    //Метод для отображения страницы книги с информацией о ней
    @GetMapping("/{id}")
    public String bookPage(@PathVariable("id") int id, @ModelAttribute("person") Person person, Model model) {
        model.addAttribute("bookPage", bookService.findById(id));

        Optional<Person> personOptional = bookService.getBookOwnerById(id);

        if (personOptional.isPresent()) {
            model.addAttribute("owner", personOptional.get());
        } else {
            model.addAttribute("personList", peopleService.findAll());
        }

        return "book/bookPage";
    }

    //Метод для создания новой книги
    @GetMapping("/new")
    public String bookCreate(Model model) {
        model.addAttribute("bookNew", new Book());
        return "book/new";
    }

    //Пост запрос принмающий данные для создания книги
    @PostMapping()
    public String bookPost(@ModelAttribute("book") Book book) {
        bookService.add(book);
        return "redirect:/books";
    }

    //Метод для редактирования книги
    @GetMapping("/{id}/edit")
    public String bookEdit(@PathVariable("id") int id, Model model) {
        model.addAttribute("bookEdit", bookService.findById(id));
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String patchMethod(@ModelAttribute("book") Book book, @PathVariable("id") int id) {
        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteMethod(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String patchMethod(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        bookService.setOwner(person.getId(), id);
        bookService.findById(id).setDate(new Date());
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/update")
    public String update(@PathVariable("id") int id) {
        bookService.setNull(id);

        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "prefix", required = false) String string, Model model) {
        if (string != null) {
            List<Book> books = bookService.findByNameStartingWith(string);
            Optional<Person> owner = bookService.getBookOwnerById(books.get(0).getId());

            if (owner.isPresent()) {
                model.addAttribute("ownerY", owner.get());
            } else {
                model.addAttribute("ownerN", owner);
            }

            model.addAttribute("book", books);
            return "book/search";
        }

        List<Book> books = new ArrayList<>();
        model.addAttribute("book", books);
        return "book/search";
    }


}
