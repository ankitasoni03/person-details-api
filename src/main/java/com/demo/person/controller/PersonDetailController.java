package com.demo.person.controller;

import com.demo.person.model.request.PersonRequest;
import com.demo.person.model.response.PersonResponse;
import com.demo.person.service.PersonDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PersonDetailController {

    @Autowired
    PersonDetailService personDetailService;


    @GetMapping("/persons")
    public ResponseEntity<List<PersonResponse>> getPersonList() {
        List<PersonResponse> personResponses = personDetailService.getPersonList();
        return new ResponseEntity<>(personResponses, HttpStatus.OK);
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<PersonResponse> getPerson(@PathVariable Long personId) {
        PersonResponse personResponse = personDetailService.getPerson(personId);
        return new ResponseEntity<>(personResponse, HttpStatus.OK);
    }

    @PostMapping("/person")
    public ResponseEntity<PersonResponse> savePerson(@RequestBody PersonRequest person) {
        return new ResponseEntity<>(personDetailService.savePerson(person), HttpStatus.CREATED);
    }

    @PutMapping("/person/{personId}")
    public ResponseEntity<PersonResponse> updatePerson(@PathVariable Long personId,
                                       @RequestBody PersonRequest person) {
        PersonResponse personResponse = personDetailService.updatePerson(personId, person);
        return new ResponseEntity<>(personResponse, HttpStatus.OK);
    }

    @DeleteMapping("/person/{personId}")
    public void deletePerson(@PathVariable Long personId) {
        personDetailService.deletePerson(personId);

    }

    @PutMapping("/person/{personId}/hobby/{hobbyId}")
    public ResponseEntity<PersonResponse> addHobbyToPerson(@PathVariable Long personId, @PathVariable Long hobbyId) {
        PersonResponse personResponse = personDetailService.addHobbyToPerson(personId, hobbyId);
        return new ResponseEntity<>(personResponse, HttpStatus.OK);
    }

    @DeleteMapping("/person/{personId}/hobby/{hobbyId}")
    public ResponseEntity<PersonResponse> removeHobbyToPerson(@PathVariable Long personId, @PathVariable Long hobbyId) {
        PersonResponse personResponse = personDetailService.removeHobbyToPerson(personId, hobbyId);
        return new ResponseEntity<>(personResponse, HttpStatus.OK);
    }

}
