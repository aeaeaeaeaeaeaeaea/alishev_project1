package ru.ae.libapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.ae.libapp.model.Book;
import ru.ae.libapp.model.Person;

import java.util.List;

@Component
public class PersonDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    //Метод для получения всех людей из БД (используем в методе контроллера personList)
    public List<Person> show() {
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    //Метод для получения человека по его ID из бд (используем в методе контроллера personPage)
    public Person getPersonById(int id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).get(0);
    }

    //Метод для редактирования данных человека из БД по его ID (используем в методе контроллера personEdit)
    public void edit(int id, Person person) {
        jdbcTemplate.update("UPDATE person SET name=?, year=? WHERE id=?", person.getName(), person.getYear(), id);
    }

    //Метод для добавления нового человека в БД
    public void add(Person person) {
        jdbcTemplate.update("INSERT INTO person(name, year) VALUES(?, ?)", person.getName(), person.getYear());
    }

    //Метод для удаления человека из БД по его ID
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
    }

    public List<Book> getListOfPersonBooks(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE person_id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class));
    }





}
