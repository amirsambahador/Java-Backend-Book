package org.j2os.repository;

import org.j2os.common.JDBC;
import org.j2os.entity.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository implements AutoCloseable {
    private Connection connection;
    private PreparedStatement preparedStatement;

    public PersonRepository() throws Exception {
        this.connection = JDBC.getConnection();
    }

    public void insert(Person person) throws Exception {
        preparedStatement = connection.prepareStatement("insert into person (name,family,age) values(?,?,?)");
        preparedStatement.setString(1, person.getName());
        preparedStatement.setString(2, person.getFamily());
        preparedStatement.setInt(3, person.getAge());
        preparedStatement.executeUpdate();
        preparedStatement = connection.prepareStatement("select last_insert_id()");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        person.setId(resultSet.getInt("last_insert_id()"));
    }

    public void update(Person person) throws Exception {
        preparedStatement = connection.prepareStatement("update person set name=?, family=?, age=? where id=?");
        preparedStatement.setString(1, person.getName());
        preparedStatement.setString(2, person.getFamily());
        preparedStatement.setInt(3, person.getAge());
        preparedStatement.setInt(4, person.getId());
        preparedStatement.executeUpdate();
    }

    public void delete(Person person) throws Exception {
        preparedStatement = connection.prepareStatement("delete from person where id=?");
        preparedStatement.setInt(1, person.getId());
        preparedStatement.executeUpdate();
    }

    public List<Person> findAll() throws Exception {
        preparedStatement = connection.prepareStatement("select * from person");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Person> persons = new ArrayList<>();
        while (resultSet.next()) {
            Person person = new Person()
                    .setId(resultSet.getInt("id"))
                    .setName(resultSet.getString("name"))
                    .setFamily(resultSet.getString("family"))
                    .setAge(resultSet.getInt("age"));
            persons.add(person);
        }
        return persons;
    }

    public void commit() throws Exception {
        connection.commit();
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
    }
}
