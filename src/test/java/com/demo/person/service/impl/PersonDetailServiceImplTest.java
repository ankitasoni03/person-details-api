package com.demo.person.service.impl;

import com.demo.person.entity.HobbyEntity;
import com.demo.person.entity.PersonEntity;
import com.demo.person.exception.NoDataFoundException;
import com.demo.person.exception.PersonDetailException;
import com.demo.person.fixture.PersonDetailFixture;
import com.demo.person.model.request.PersonRequest;
import com.demo.person.model.response.PersonResponse;
import com.demo.person.repository.HobbyRepository;
import com.demo.person.repository.PersonDetailRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class PersonDetailServiceImplTest {

    @InjectMocks
    PersonDetailServiceImpl personDetailService = new PersonDetailServiceImpl();

    @Mock
    PersonDetailRepository personDetailRepository;

    @Mock
    HobbyRepository hobbyRepository;

    @Test
    void getPersonListTest() {
        when(personDetailRepository.findAll()).thenReturn(PersonDetailFixture.getPersonEntityList());
        List<PersonResponse> personResponseList = personDetailService.getPersonList();
        assertEquals(2, personResponseList.size());
    }

    @Test
    void getPersonListTest_EmptyList() {
        when(personDetailRepository.findAll()).thenReturn(new ArrayList<>());
        List<PersonResponse> personResponseList = personDetailService.getPersonList();
        assertEquals(0, personResponseList.size());
    }

    @Test
    void getPersonListTest_Exception() {
        when(personDetailRepository.findAll()).thenThrow(new RuntimeException());
        PersonDetailException personDetailException = Assertions.assertThrows(PersonDetailException.class,
                () -> personDetailService.getPersonList());
        assertEquals("Exception occurred while fetching person list", personDetailException.getMessage());
    }

    @Test
    void getPersonTest() {
        when(personDetailRepository.findById(anyLong()))
                .thenReturn(Optional.of(PersonDetailFixture.getPersonEntityList().get(0)));

        PersonResponse personResponse = personDetailService.getPerson(1L);
        assertEquals("John", personResponse.getFirstName());
    }

    @Test
    void getPersonTest_PersonNotFound() {
        when(personDetailRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        NoDataFoundException noDataFoundException = Assertions.assertThrows(NoDataFoundException.class,
                () -> personDetailService.getPerson(1L));

        assertEquals("Person not found in DB", noDataFoundException.getMessage());
    }

    @Test
    void getPersonTest_Exception() {
        when(personDetailRepository.findById(anyLong())).thenThrow(new RuntimeException());

        PersonDetailException personDetailException = Assertions.assertThrows(PersonDetailException.class,
                () -> personDetailService.getPerson(1L));

        assertEquals("Exception occurred while fetching person", personDetailException.getMessage());
    }

    @Test
    void savePersonTest() {
        when(personDetailRepository.save(Mockito.any(PersonEntity.class)))
                .thenReturn(PersonDetailFixture.getPersonEntityList().get(0));

        PersonResponse personResponse = personDetailService.savePerson(PersonDetailFixture.getPersonRequest());
        assertEquals("John", personResponse.getFirstName());
    }

    @Test
    void savePersonTest_Exception() {
        when(personDetailRepository.save(Mockito.any(PersonEntity.class))).thenThrow(new RuntimeException());
        PersonRequest personRequest = PersonDetailFixture.getPersonRequest();
        PersonDetailException personDetailException = Assertions.assertThrows(PersonDetailException.class,
                () -> personDetailService.savePerson(personRequest));

        assertEquals("Exception occurred while saving person", personDetailException.getMessage());
    }

    @Test
    void updatePersonTest() {
        PersonEntity personEntity = PersonDetailFixture.getPersonEntityList().get(0);
        when(personDetailRepository.findById(anyLong())).thenReturn(Optional.of(personEntity));
        personEntity.setAge("55");
        when(personDetailRepository.save(Mockito.any(PersonEntity.class))).thenReturn(personEntity);
        PersonRequest personRequest = PersonDetailFixture.getPersonRequest();
        personRequest.setAge("55");

        PersonResponse personResponse = personDetailService.updatePerson(1L, personRequest);
        assertEquals("55", personResponse.getAge());
    }

    @Test
    void updatePersonTest_NoDataFoundExp() {
        when(personDetailRepository.findById(anyLong())).thenReturn(Optional.empty());
        PersonRequest personRequest = PersonDetailFixture.getPersonRequest();
        personRequest.setAge("55");

        NoDataFoundException noDataFoundException = Assertions.assertThrows(NoDataFoundException.class,
                () -> personDetailService.updatePerson(1L, personRequest));

        assertEquals("Person cannot be found", noDataFoundException.getMessage());
    }

    @Test
    void updatePersonTest_Exception() {
        when(personDetailRepository.findById(anyLong())).thenReturn(Optional.of(PersonDetailFixture.getPersonEntityList().get(0)));
        when(personDetailRepository.save(Mockito.any(PersonEntity.class))).thenThrow(new RuntimeException());
        PersonRequest personRequest = PersonDetailFixture.getPersonRequest();
        personRequest.setAge("55");

        PersonDetailException personDetailException = Assertions.assertThrows(PersonDetailException.class,
                () -> personDetailService.updatePerson(1L, personRequest));

        assertEquals("Error occurred while updating person", personDetailException.getMessage());
    }


    @Test
    void deletePersonTest() {
        PersonEntity personEntity = PersonDetailFixture.getPersonEntityList().get(0);
        when(personDetailRepository.findById(anyLong()))
                .thenReturn(Optional.of(personEntity));
        doNothing().when(personDetailRepository).delete(Mockito.any(PersonEntity.class));

        personDetailService.deletePerson(1L);
        Mockito.verify(personDetailRepository).findById(1L);
        Mockito.verify(personDetailRepository).delete(personEntity);
    }

    @Test
    void deletePersonTest_PersonNotFound() {
        when(personDetailRepository.findById(anyLong())).thenReturn(Optional.empty());
        NoDataFoundException noDataFoundException = Assertions.assertThrows(NoDataFoundException.class,
                () -> personDetailService.deletePerson(1L));
        assertEquals("Person not found", noDataFoundException.getMessage());
    }

    @Test
    void deletePersonTest_Exception() {
        when(personDetailRepository.findById(anyLong())).thenReturn(Optional.of(PersonDetailFixture.getPersonEntityList().get(0)));
        doThrow(new RuntimeException()).when(personDetailRepository).delete(any(PersonEntity.class));

        PersonDetailException personDetailException = Assertions.assertThrows(PersonDetailException.class,
                () -> personDetailService.deletePerson(1L));

        assertEquals("Exception occurred while deleting person", personDetailException.getMessage());
    }

    @Test
    void addHobbyToPersonTest() {
        PersonEntity personEntity = PersonDetailFixture.getPersonEntityList().get(1);
        HobbyEntity hobbyEntity = PersonDetailFixture.getHobbyEntityList().get(2);
        when(personDetailRepository.findById(anyLong())).thenReturn(Optional.of(personEntity));
        when(hobbyRepository.findById(anyLong())).thenReturn(Optional.of(hobbyEntity));
        hobbyEntity.setPersonEntity(personEntity);
        when(hobbyRepository.save(any(HobbyEntity.class))).thenReturn(hobbyEntity);

        PersonResponse personResponse = personDetailService.addHobbyToPerson(2L, 3L);
        assertEquals("chess",personResponse.getHobby().get(0).getHobby());

    }

    @Test
    void addHobbyToPersonTest_PersonNotFound() {
        when(personDetailRepository.findById(anyLong())).thenReturn(Optional.empty());

        NoDataFoundException noDataFoundException = Assertions.assertThrows(NoDataFoundException.class,
                () -> personDetailService.addHobbyToPerson(2L, 3L));

        assertEquals("Person not found",noDataFoundException.getMessage());

    }

    @Test
    void addHobbyToPersonTest_HobbyNotFound() {
        PersonEntity personEntity = PersonDetailFixture.getPersonEntityList().get(1);
        HobbyEntity hobbyEntity = PersonDetailFixture.getHobbyEntityList().get(2);
        when(personDetailRepository.findById(anyLong())).thenReturn(Optional.of(personEntity));
        when(hobbyRepository.findById(anyLong())).thenReturn(Optional.empty());
        NoDataFoundException noDataFoundException = Assertions.assertThrows(NoDataFoundException.class,
                () -> personDetailService.addHobbyToPerson(2L, 3L));

        assertEquals("Hobby not found",noDataFoundException.getMessage());

    }

    @Test
    void addHobbyToPersonTest_Exception() {
        PersonEntity personEntity = PersonDetailFixture.getPersonEntityList().get(1);
        HobbyEntity hobbyEntity = PersonDetailFixture.getHobbyEntityList().get(2);
        when(personDetailRepository.findById(anyLong())).thenReturn(Optional.of(personEntity));
        when(hobbyRepository.findById(anyLong())).thenReturn(Optional.of(hobbyEntity));
        hobbyEntity.setPersonEntity(personEntity);
        when(hobbyRepository.save(any(HobbyEntity.class))).thenThrow(new RuntimeException());

        Exception exception = Assertions.assertThrows(Exception.class,
                () -> personDetailService.addHobbyToPerson(2L, 3L));

        assertEquals("Exception occurred while adding hobby of person", exception.getMessage());

    }

    @Test
    void removeHobbyToPersonTest() {
        PersonEntity personEntity = PersonDetailFixture.getPersonEntityList().get(1);
        HobbyEntity hobbyEntity = PersonDetailFixture.getHobbyEntityList().get(2);
        when(personDetailRepository.findById(anyLong())).thenReturn(Optional.of(personEntity));
        when(hobbyRepository.findById(anyLong())).thenReturn(Optional.of(hobbyEntity));
        hobbyEntity.setPersonEntity(null);
        when(hobbyRepository.save(any(HobbyEntity.class))).thenReturn(hobbyEntity);

        PersonResponse personResponse = personDetailService.removeHobbyToPerson(2L, 3L);
        assertEquals(0,personResponse.getHobby().size());

    }

    @Test
    void removeHobbyToPersonTest_PersonNotFound() {
        when(personDetailRepository.findById(anyLong())).thenReturn(Optional.empty());

        NoDataFoundException noDataFoundException = Assertions.assertThrows(NoDataFoundException.class,
                () -> personDetailService.removeHobbyToPerson(2L, 3L));

        assertEquals("Person not found",noDataFoundException.getMessage());

    }

    @Test
    void removeHobbyToPersonTest_HobbyNotFound() {
        PersonEntity personEntity = PersonDetailFixture.getPersonEntityList().get(1);
        when(personDetailRepository.findById(anyLong())).thenReturn(Optional.of(personEntity));
        when(hobbyRepository.findById(anyLong())).thenReturn(Optional.empty());
        NoDataFoundException noDataFoundException = Assertions.assertThrows(NoDataFoundException.class,
                () -> personDetailService.removeHobbyToPerson(2L, 3L));

        assertEquals("Hobby not found",noDataFoundException.getMessage());

    }

    @Test
    void removeHobbyToPersonTest_Exception() {
        PersonEntity personEntity = PersonDetailFixture.getPersonEntityList().get(1);
        HobbyEntity hobbyEntity = PersonDetailFixture.getHobbyEntityList().get(2);
        when(personDetailRepository.findById(anyLong())).thenReturn(Optional.of(personEntity));
        when(hobbyRepository.findById(anyLong())).thenReturn(Optional.of(hobbyEntity));
        hobbyEntity.setPersonEntity(null);
        when(hobbyRepository.save(any(HobbyEntity.class))).thenThrow(new RuntimeException());

        Exception exception = Assertions.assertThrows(Exception.class,
                () -> personDetailService.removeHobbyToPerson(2L, 3L));

        assertEquals("Exception occurred while removing hobby of person", exception.getMessage());

    }

}
