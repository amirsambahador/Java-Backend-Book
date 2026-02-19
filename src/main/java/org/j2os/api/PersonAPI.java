package org.j2os.api;

import jakarta.ws.rs.*;
import lombok.extern.log4j.Log4j2;
import org.j2os.entity.Person;
import org.j2os.service.PersonService;

import java.util.Map;

@Path("/person")
@Log4j2
public class PersonAPI {
    @Path("/save")
    @POST
    @Produces("application/json")
    public Object save(@FormParam("name") String name, @FormParam("family") String family, @FormParam("age") String age) {
        try {
            Person person = new Person().setName(name).setFamily(family).setAge(Integer.parseInt(age));
            PersonService.getInstance().save(person);
            return person;
        } catch (Exception e) {
            log.error(e);
            return Map.of("message", "person save failed");
        }
    }

    @Path("/remove")
    @POST
    @Produces("application/json")
    public Object remove(@FormParam("id") String id) {
        try {
            Person person = new Person().setId(Integer.parseInt(id));
            PersonService.getInstance().remove(person);
            return person;
        } catch (Exception e) {
            log.error(e);
            return Map.of("message", "person remove failed");
        }
    }

    @Path("/update")
    @POST
    @Produces("application/json")
    public Object update(@FormParam("id") String id, @FormParam("name") String name, @FormParam("family") String family, @FormParam("age") String age) {
        try {
            Person person = new Person().setId(Integer.parseInt(id)).setName(name).setFamily(family).setAge(Integer.parseInt(age));
            PersonService.getInstance().update(person);
            return person;
        } catch (Exception e) {
            log.error(e);
            return Map.of("message", "person update failed");
        }
    }

    @Path("/findAll")
    @GET
    @Produces("application/json")
    public Object findAll() {
        try {
            return PersonService.getInstance().findAll();
        } catch (Exception e) {
            log.error(e);
            return Map.of("message", "person findAll failed");
        }
    }
}