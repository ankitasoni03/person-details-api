package com.demo.person.controller;

import com.demo.person.model.request.HobbyRequest;
import com.demo.person.model.response.HobbyResponse;
import com.demo.person.service.HobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HobbyController {

    @Autowired
    HobbyService hobbyService;

    @GetMapping("/hobbies")
    public ResponseEntity<List<HobbyResponse>> getHobbies() {
        List<HobbyResponse> hobbies = hobbyService.getHobbies();
        return new ResponseEntity<>(hobbies, HttpStatus.OK);
    }

    @GetMapping("/hobby/{hobbyId}")
    public ResponseEntity<HobbyResponse> getHobby(@PathVariable Long hobbyId) {
        HobbyResponse hobbies = hobbyService.getHobby(hobbyId);
        return new ResponseEntity<>(hobbies, HttpStatus.OK);
    }

    @PostMapping("/hobby")
    public ResponseEntity<HobbyResponse> saveHobby(@RequestBody HobbyRequest hobbyRequest) {
        HobbyResponse hobbyResponse = hobbyService.saveHobby(hobbyRequest);
        return new ResponseEntity<>(hobbyResponse, HttpStatus.CREATED);
    }


    @PutMapping("/hobby/{hobbyId}")
    public ResponseEntity<HobbyResponse> updateHobby(@PathVariable Long hobbyId, @RequestBody HobbyRequest hobbyRequest) {
        HobbyResponse hobbies = hobbyService.updateHobby(hobbyId, hobbyRequest);
        return new ResponseEntity<>(hobbies, HttpStatus.OK);
    }

    @DeleteMapping("/hobby/{hobbyId}")
    public void deleteHobby(@PathVariable Long hobbyId) {
        hobbyService.deleteHobby(hobbyId);
    }


}
