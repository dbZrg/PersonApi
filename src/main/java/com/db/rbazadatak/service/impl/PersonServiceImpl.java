package com.db.rbazadatak.service;

import com.db.rbazadatak.model.Person;
import com.db.rbazadatak.payload.PersonDTO;
import com.db.rbazadatak.repository.PersonRepository;
import com.db.rbazadatak.utils.FileUtils;
import com.db.rbazadatak.utils.OibValidation;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PersonService {
    PersonRepository userRepository;
    ModelMapper modelMapper;

    @Autowired
    PersonService(PersonRepository userRepository) {
        this.userRepository = userRepository;
        this.modelMapper = new ModelMapper();
    }

    public ResponseEntity<Person> savePerson(PersonDTO personDTO) {
        OibValidation.checkOIB(personDTO.getOib());

        Optional<Person> person = userRepository.findByOib(personDTO.getOib());
        if (person.isPresent()) {
            throw new IllegalArgumentException("Person with OIB " + personDTO.getOib() + " already exists.");
        }

        Person personToSave = new Person();
        modelMapper.map(personDTO, personToSave);
        Person savedPerson = userRepository.save(personToSave);
        return ResponseEntity.ok().body(savedPerson);
    }

    public ResponseEntity<Person> findPersonByOib(String oib) {
        OibValidation.checkOIB(oib);

        Person person = userRepository.findByOib(oib)
                .orElseThrow(() -> new EntityNotFoundException("Person with: " + oib + " doesnt exist."));
        FileUtils.generateFile(person);
        return ResponseEntity.ok().body(person);
    }

    public ResponseEntity<String> deletePerson(String oib) {
        OibValidation.checkOIB(oib);

        Optional<Person> person = userRepository.findByOib(oib);
        if (person.isPresent()) {
            userRepository.delete(person.get());
            FileUtils.setInactiveStatus(person.get());
            return ResponseEntity.ok("Person with OIB:" + person.get().getOib() + " successfully deleted");
        } else {
            throw new IllegalArgumentException("Person with: " + oib + " doesnt exist.");
        }

    }

}
