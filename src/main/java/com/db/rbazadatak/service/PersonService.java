package com.db.rbazadatak.service;

import com.db.rbazadatak.model.Person;
import com.db.rbazadatak.payload.PersonDTO;
import org.springframework.http.ResponseEntity;

public interface PersonService {
    ResponseEntity<Person> savePerson(PersonDTO personDTO);

    ResponseEntity<Person> findPersonByOib(String oib, Boolean newFile);

    ResponseEntity<String> deletePerson(String oib, Boolean fullDelete);
}
