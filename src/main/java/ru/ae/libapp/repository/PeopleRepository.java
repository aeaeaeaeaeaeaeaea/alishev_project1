package ru.ae.libapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ae.libapp.model.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
}
