package org.j2os.service;

import lombok.SneakyThrows;
import org.j2os.entity.Person;
import org.j2os.repository.PersonRepository;

import java.util.List;

public class PersonService {
    private static final PersonService INSTANCE = new PersonService();

    private PersonService() {
    }

    public static PersonService getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    public void save(Person person) {
        person.setAge(person.getAge() - 1);
        try (PersonRepository repository = new PersonRepository()) {
            repository.insert(person);
            repository.commit();
        }
    }

    @SneakyThrows
    public void update(Person person) {
        person.setAge(person.getAge() - 1);
        try (PersonRepository repository = new PersonRepository()) {
            repository.update(person);
            repository.commit();
        }
    }

    @SneakyThrows
    public void remove(Person person) {
        try (PersonRepository repository = new PersonRepository()) {
            repository.delete(person);
            repository.commit();
        }
    }

    @SneakyThrows
    public List<Person> findAll() {
        try (PersonRepository repository = new PersonRepository()) {
            return repository.findAll();

        }
    }
}
