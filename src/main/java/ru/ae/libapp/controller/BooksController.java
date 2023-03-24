package ru.ae.libapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ae.libapp.dao.BookDao;
import ru.ae.libapp.dao.PersonDao;
import ru.ae.libapp.model.Book;
import ru.ae.libapp.model.Person;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookDao bookDao;
    private final PersonDao personDao;

    @Autowired
    public BooksController(BookDao bookDao, PersonDao personDao) {
        this.bookDao = bookDao;
        this.personDao = personDao;
    }

    //Метод для отображения всех книг
    @GetMapping()
    public String bookList(Model model) {
        model.addAttribute("bookList", bookDao.show());
        return "book/books";
    }

    //Метод для отображения страницы книги с информацией о ней
    @GetMapping("/{id}")
    public String bookPage(@PathVariable("id") int id, @ModelAttribute("person") Person person, Model model) {
        model.addAttribute("bookPage", bookDao.getBookById(id));

        Optional<Person> personOptional = bookDao.getOwnerBookById(id);

        if(personOptional.isPresent()) {
            model.addAttribute("owner", personOptional.get());
        } else {
            model.addAttribute("personList", personDao.show());
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
        bookDao.createBook(book);
        return "redirect:/books";
    }

    //Метод для редактирования книги
    @GetMapping("/{id}/edit")
    public String bookEdit(@PathVariable("id") int id, Model model) {
        model.addAttribute("bookEdit", bookDao.getBookById(id));
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String patchMethod(@ModelAttribute("book") Book book, @PathVariable("id") int id) {
        bookDao.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteMethod(@PathVariable("id") int id) {
        bookDao.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String patchMethod(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        bookDao.addForeignKey(person.getId(), id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/update")
    public String update(@PathVariable("id") int id) {
        bookDao.setNull(id);
        return "redirect:/books/" + id;
    }




}
