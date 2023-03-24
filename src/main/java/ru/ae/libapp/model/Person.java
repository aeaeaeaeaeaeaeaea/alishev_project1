package ru.ae.libapp.model;

public class Person {

    private int id;
    private String name;
    private int year;

    public Person(String name, int year, int id) {
        this.name = name;
        this.year = year;
        this.id = id;
    }

    public Person() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
