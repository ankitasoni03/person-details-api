package com.demo.person.service;

import com.demo.person.model.request.PersonRequest;
import com.demo.person.model.response.PersonResponse;

import java.util.List;

/**
 * Service class for all Person related methods
 */
public interface PersonDetailService {

    List<PersonResponse> getPersonList();
    PersonResponse getPerson(Long personId);
    PersonResponse savePerson(PersonRequest personRequest);
    PersonResponse updatePerson(Long personId, PersonRequest personRequest);
    void deletePerson(Long personId);

    PersonResponse addHobbyToPerson(Long personId, Long hobbyId);

    PersonResponse removeHobbyToPerson(Long personId, Long hobbyId);
}
