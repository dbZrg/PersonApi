package com.db.rbazadatak.service.impl;

import com.db.rbazadatak.model.File;
import com.db.rbazadatak.model.Person;
import com.db.rbazadatak.payload.PersonDTO;
import com.db.rbazadatak.repository.PersonRepository;
import com.db.rbazadatak.service.PersonService;
import com.db.rbazadatak.utils.OibValidation;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class PersonServiceImpl implements PersonService {
    final PersonRepository personRepository;
    final ModelMapper modelMapper;

    final FileServiceImpl fileService;

    @Autowired
    PersonServiceImpl(PersonRepository personRepository, FileServiceImpl fileService) {
        this.personRepository = personRepository;
        this.fileService = fileService;
        this.modelMapper = new ModelMapper();
    }

    public ResponseEntity<Person> savePerson(PersonDTO personDTO) {
        OibValidation.checkOIB(personDTO.getOib());

        Optional<Person> person = personRepository.findByOib(personDTO.getOib());
        if (person.isPresent()) {
            throw new IllegalArgumentException("Person with OIB " + personDTO.getOib() + " already exists.");
        }

        Person personToSave = new Person();
        modelMapper.map(personDTO, personToSave);
        Person savedPerson = personRepository.save(personToSave);
        return ResponseEntity.ok().body(savedPerson);
    }

    @Transactional
    public ResponseEntity<Person> findPersonByOib(String oib, Boolean newFile) throws IOException {
        OibValidation.checkOIB(oib);

        Person person = personRepository.findByOib(oib)
                .orElseThrow(() -> new EntityNotFoundException("Person with: " + oib + " doesnt exist."));

        File personFile = new File();
        modelMapper.map(person, personFile);
        File file = fileService.createFile(personFile);
        person.setFile(file);
        personRepository.save(person);
        return ResponseEntity.ok().body(person);
    }

    @Transactional
    public ResponseEntity<String> deletePerson(String oib) throws IOException {
        OibValidation.checkOIB(oib);

        Optional<Person> person = personRepository.findByOib(oib);
        if (person.isPresent()) {
            personRepository.delete(person.get());
            fileService.setFileStatusToInactive(person.get());
            return ResponseEntity.ok("Person with OIB:" + person.get().getOib() + " successfully deleted");
        } else {
            throw new EntityNotFoundException("Person with: " + oib + " doesnt exist.");
        }

    }

}
