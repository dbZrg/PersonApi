package com.db.rbazadatak.service.impl;

import com.db.rbazadatak.model.Person;
import com.db.rbazadatak.payload.PersonDTO;
import com.db.rbazadatak.repository.PersonRepository;
import com.db.rbazadatak.service.PersonService;
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
public class PersonServiceImpl implements PersonService {
    final PersonRepository userRepository;
    final ModelMapper modelMapper;

    @Autowired
    PersonServiceImpl(PersonRepository userRepository) {
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

    public ResponseEntity<Person> findPersonByOib(String oib, Boolean newFile) {
        OibValidation.checkOIB(oib);

        Person person = userRepository.findByOib(oib)
                .orElseThrow(() -> new EntityNotFoundException("Person with: " + oib + " doesnt exist."));

        // In case we want to generate new file for person (example: person data changed)
        // Set current active file status to INACTIVE
        if (newFile) {
            FileUtils.setInactiveStatus(person);
        }
        // Generate new file for person
        FileUtils.generateFile(person);
        return ResponseEntity.ok().body(person);
    }

    public ResponseEntity<String> deletePerson(String oib, Boolean deleteAll) {
        OibValidation.checkOIB(oib);

        Optional<Person> person = userRepository.findByOib(oib);
        if (person.isPresent()) {
            userRepository.delete(person.get());
            if (deleteAll) {
                // Delete all files for person, active and inactive
                FileUtils.deleteAllFilesForPerson(person.get());
            } else {
                // Set persons active file to INACTIVE
                FileUtils.setInactiveStatus(person.get());
            }
            return ResponseEntity.ok("Person with OIB:" + person.get().getOib() + " successfully deleted");
        } else {
            throw new EntityNotFoundException("Person with: " + oib + " doesnt exist.");
        }

    }

}
