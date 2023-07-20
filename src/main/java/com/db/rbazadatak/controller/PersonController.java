package com.db.rbazadatak.controller;

import com.db.rbazadatak.model.Person;
import com.db.rbazadatak.payload.PersonDTO;
import com.db.rbazadatak.service.impl.PersonServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/person/")
@CrossOrigin
public class PersonController {

    final PersonServiceImpl personService;

    @Autowired
    public PersonController(PersonServiceImpl personService) {
        this.personService = personService;
    }


    @PostMapping
    @Operation(summary = "Add a new person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Person.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request - Illegal Argument, Constraint Violation or Malformed JSON",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found - The specified person does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Person> addPerson(@Valid @RequestBody PersonDTO person) {
        return personService.savePerson(person);
    }


    @GetMapping("/{oib}")
    @Operation(summary = "Find a person by OIB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Person.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request - Illegal Argument, Constraint Violation or Malformed JSON",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found - The specified person does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Person> findPerson(@PathVariable String oib, @RequestParam(defaultValue = "false") Boolean newFile) {
        return personService.findPersonByOib(oib, newFile);
    }


    @DeleteMapping("/{oib}")
    @Operation(summary = "Delete a person by OIB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request - Illegal Argument, Constraint Violation or Malformed JSON",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found - The specified person does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<String> deletePerson(@PathVariable String oib, @RequestParam(defaultValue = "false") Boolean deleteAll) throws IOException {
        return personService.deletePerson(oib, deleteAll);
    }
}
