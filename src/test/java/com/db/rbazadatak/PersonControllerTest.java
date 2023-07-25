package com.db.rbazadatak;

import com.db.rbazadatak.model.Person;
import com.db.rbazadatak.model.Status;
import com.db.rbazadatak.payload.PersonDTO;
import com.db.rbazadatak.repository.PersonRepository;
import com.db.rbazadatak.utils.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${app.basic.auth.username}")
    private String username;

    @Value("${app.basic.auth.password}")
    private String password;

    @Autowired
    private PersonRepository personRepository;

    private static Person person;

    @BeforeAll
    static void setUpAll() {
        person = new Person();
        person.setOib("83972579593");
        person.setFirstName("Name");
        person.setLastName("LastName");
        person.setStatus(Status.ACTIVE);
    }

    @BeforeEach
    public void setUp() {
        personRepository.deleteAll();
    }

    @Test
    public void testAddPerson() throws Exception {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setOib("83972579593");
        personDTO.setFirstName("Name");
        personDTO.setLastName("LastName");
        personDTO.setStatus(Status.ACTIVE);

        mockMvc.perform(post("/api/person/")
                        .with(httpBasic(username, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(personDTO)))
                .andExpect(status().isOk());

        Optional<Person> optionalSavedPerson = personRepository.findByOib(personDTO.getOib());
        assertTrue(optionalSavedPerson.isPresent());

        Person savedPerson = optionalSavedPerson.get();
        assertEquals(personDTO.getOib(), savedPerson.getOib());
        assertEquals(personDTO.getFirstName(), savedPerson.getFirstName());
        assertEquals(personDTO.getLastName(), savedPerson.getLastName());
        assertEquals(personDTO.getStatus(), savedPerson.getStatus());
    }

    @Test
    public void testGetPerson() throws Exception {
        personRepository.save(person);

        mockMvc.perform(get("/api/person/" + person.getOib())
                        .with(httpBasic(username, password)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.oib").value(person.getOib()))
                .andExpect(jsonPath("$.firstName").value(person.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(person.getLastName()))
                .andExpect(jsonPath("$.status").value(person.getStatus().toString()));

        Optional<File> activeFile = FileUtils.findActiveFile(person.getOib());
        assertTrue(activeFile.isPresent());
        activeFile.get().delete();
    }

    @Test
    public void testDeletePerson() throws Exception {

        personRepository.save(person);

        mockMvc.perform(get("/api/person/" + person.getOib())
                        .with(httpBasic(username, password)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.oib").value(person.getOib()))
                .andExpect(jsonPath("$.firstName").value(person.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(person.getLastName()))
                .andExpect(jsonPath("$.status").value(person.getStatus().toString()));

        mockMvc.perform(delete("/api/person/" + person.getOib())
                        .with(httpBasic(username, password)))
                .andExpect(status().isOk());

        Optional<Person> deletedPerson = personRepository.findByOib(person.getOib());
        assertTrue(deletedPerson.isEmpty());

        Optional<File> inactiveFile = FileUtils.findFileByOib(person.getOib());
        assertTrue(inactiveFile.isPresent());
        assertTrue(FileUtils.containsStatus(inactiveFile.get(), Status.INACTIVE));
        inactiveFile.get().delete();
    }
}
