package ru.ae.libapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.ae.libapp.model.Book;
import ru.ae.libapp.model.Person;

import java.util.List;
import java.util.Optional;

@Component
public class BookDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Метод для получения всех книг из БД
    public List<Book> show() {
        return jdbcTemplate.query("SELECT * FROM book", new BeanPropertyRowMapper<>(Book.class));
    }

    //Метод для получения книги по ID из БД
    public Book getBookById(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class)).get(0);
    }

    //Метод для создания новой книги
    public void createBook(Book book) {
        jdbcTemplate.update("INSERT INTO book(name, author, year) VALUES(?, ?, ?)", book.getName(), book.getAuthor(), book.getYear());
    }

    //Метод для обновления книги
    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE book SET name=?, author=?, year=? WHERE id=?", book.getName(), book.getAuthor(), book.getYear(), id);
    }

    //Метод для удаления книги из БД по ID
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }

    public void addForeignKey(int id1, int id2) {
        jdbcTemplate.update("UPDATE book SET person_id=? WHERE id=?", id1, id2);
    }

    //Join метод для получения владельца книги
    public Optional<Person> getOwnerBookById(int id) {
        return jdbcTemplate.query("SELECT person.* FROM book JOIN person ON book.person_id = person.id WHERE book.id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    public void setNull(int id) {
        jdbcTemplate.update("UPDATE book SET person_id=NULL WHERE id=?", id);
    }


}
