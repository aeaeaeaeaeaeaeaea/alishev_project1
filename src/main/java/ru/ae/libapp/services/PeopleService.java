package ru.ae.libapp.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ae.libapp.model.Book;
import ru.ae.libapp.model.Person;
import ru.ae.libapp.repository.BookRepository;
import ru.ae.libapp.repository.PeopleRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final BookRepository bookRepository;

    @Autowired
    public PeopleService(BookRepository bookRepository, PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
        this.bookRepository = bookRepository;
    }

    //Метод для получения всех людей из БД
    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public List<Person> findAll(int page, int itemsPerPage) {
        return peopleRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
    }

    public List<Person> findAll(String year) {
        return peopleRepository.findAll(Sort.by(year));
    }

    public List<Person> findAll(int page, int itemsPerPage, String year) {
        return peopleRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by(year))).getContent();
    }

    //Метод для получения человека по его ID
    public Person findById(int id) {
        return peopleRepository.findById(id).get();
    }

    //Метод для обновления данных человека по его ID
    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        peopleRepository.save(person);
    }

    //Метод для добавления нового человека в БД
    @Transactional
    public void add(Person person) {
        peopleRepository.save(person);
    }

    //Метод для удаления человка по его ID
    @Transactional
    public void delete(int id) {
        List<Book> books = findById(id).getBooks();
        for (Book book : books) {
            book.setOwner(null);
        }
        peopleRepository.deleteById(id);
    }

    //Метод для получения всех книг человека
    public List<Book> getPersonsBooksById(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            return person.get().getBooks();
        }

        return Collections.emptyList();
    }


}
