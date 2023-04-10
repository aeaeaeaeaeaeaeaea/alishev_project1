package ru.ae.libapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ae.libapp.model.Book;
import ru.ae.libapp.model.Person;
import ru.ae.libapp.repository.BookRepository;
import ru.ae.libapp.repository.PeopleRepository;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BookService(PeopleRepository peopleRepository, BookRepository bookRepository) {
        this.peopleRepository = peopleRepository;
        this.bookRepository = bookRepository;
    }

    //Метод для получения всех книг из БД
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findAll(int page, int itemsPerPage) {
        return bookRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
    }

    //Метод для получения книги по ее ID из БД
    public Book findById(int id) {
        return bookRepository.findById(id).get();
    }

    //Метод для добавления новой книги
    @Transactional
    public void add(Book book) {
        bookRepository.save(book);
    }

    //Метод для обновления книги по ее ID
    @Transactional
    public void update(int id, Book book) {
        book.setId(id);
        bookRepository.save(book);
    }

    //Метод для удаления книги по ее ID
    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    //Метод для получения владельца книги по ее ID
    public Optional<Person> getBookOwnerById(int id) {
        Optional<Book> book = bookRepository.findById(id);
        Optional<Person> person = Optional.ofNullable(book.get().getOwner());
        return person;
    }

    //Метод для назначения книги человеку
    @Transactional
    public void setOwner(int person_id, int id) {
        Optional<Book> book = bookRepository.findById(id);
        Person person = peopleRepository.findById(person_id).get();
        Date specificDate = new Date(1220227200000L);
        book.get().setDate(specificDate);
        book.get().setOwner(person);
    }

    //Метод для обнуления person_id
    @Transactional
    public void setNull(int id) {
        Optional<Book> book = bookRepository.findById(id);
        book.get().setOwner(null);
    }

    //Метод для поиска книги по ее первым буквам
    public List<Book> findByNameStartingWith(String prefix) {
        return bookRepository.findByNameStartingWith(prefix);
    }




}
