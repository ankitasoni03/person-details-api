package com.demo.person.service.impl;

import com.demo.person.entity.HobbyEntity;
import com.demo.person.entity.PersonEntity;
import com.demo.person.exception.NoDataFoundException;
import com.demo.person.exception.PersonDetailException;
import com.demo.person.model.response.HobbyResponse;
import com.demo.person.model.request.PersonRequest;
import com.demo.person.model.response.PersonResponse;
import com.demo.person.repository.HobbyRepository;
import com.demo.person.repository.PersonDetailRepository;
import com.demo.person.service.PersonDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonDetailServiceImpl implements PersonDetailService {

    private final Logger logger = LoggerFactory.getLogger(PersonDetailServiceImpl.class);

    @Autowired
    PersonDetailRepository personDetailRepository;

    @Autowired
    HobbyRepository hobbyRepository;

    @Override
    public List<PersonResponse> getPersonList() {
        List<PersonResponse> personResponseList = new ArrayList<>();
        List<PersonEntity> personEntityList = null;
        try {
            personEntityList = personDetailRepository.findAll();

        } catch (Exception exception) {
            logger.error("Exception occurred while fetching person list", exception);
            throw new PersonDetailException("Exception occurred while fetching person list");
        }
        if (CollectionUtils.isEmpty(personEntityList)) {
            logger.warn("Person list is empty");
            return personResponseList;
        }

        buildPersonResponseList(personResponseList, personEntityList);
        return personResponseList;
    }


    @Override
    public PersonResponse getPerson(Long personId) {
        Optional<PersonEntity> personEntity;
        try {
            personEntity = personDetailRepository.findById(personId);
        } catch (Exception exception) {
            logger.error("Exception occurred while fetching person [{}]", personId, exception);
            throw new PersonDetailException("Exception occurred while fetching person");
        }

        if (personEntity.isEmpty()) {
            logger.error("Person [{}] not found in database", personId);
            throw new NoDataFoundException("Person not found in DB");
        }

        return buildPersonResponse(personEntity.get());
    }

    @Override
    public PersonResponse savePerson(PersonRequest personRequest) {
        PersonResponse personResponse;
        try {
            PersonEntity personEntity = buildPersonEntity(personRequest);
            personEntity = personDetailRepository.save(personEntity);
            personResponse = PersonResponse.builder()
                    .personId(personEntity.getPersonId())
                    .firstName(personEntity.getFirstName())
                    .lastName(personEntity.getLastName())
                    .age(personEntity.getAge())
                    .favouriteColor(personEntity.getFavouriteColor())
                    .build();
        } catch (Exception exception) {
            logger.error("Exception occurred while saving person [{}]", personRequest.getFirstName(), exception);
            throw new PersonDetailException("Exception occurred while saving person");
        }

        return personResponse;

    }

    @Override
    public PersonResponse updatePerson(Long personId, PersonRequest personRequest) {
        PersonResponse personResponse;
        try {
            Optional<PersonEntity> personEntity = personDetailRepository.findById(personId);

            if (personEntity.isEmpty()) {
                logger.error("Person [{}] not found", personId);
                throw new NoDataFoundException("Person cannot be found");
            }

            buildPersonEntityForUpdate(personEntity.get(), personRequest);
            PersonEntity updatedResponse = personDetailRepository.save(personEntity.get());
            personResponse = buildPersonResponse(updatedResponse);

        } catch (NoDataFoundException noDataFoundException) {
            throw noDataFoundException;
        } catch (Exception exception) {
            logger.error("Error occurred while updating person [{}] details", personId, exception);
            throw new PersonDetailException("Error occurred while updating person");
        }


        return personResponse;

    }

    @Override
    public void deletePerson(Long personId) {
        try {
            Optional<PersonEntity> personEntity = personDetailRepository.findById(personId);

            if (personEntity.isEmpty()) {
                logger.error("Person [{}] not found", personId);
                throw new NoDataFoundException("Person not found");
            }

            personDetailRepository.delete(personEntity.get());
        } catch (NoDataFoundException noDataFoundException) {
            throw noDataFoundException;
        } catch (Exception exception) {
            logger.error("Exception occurred while deleting person [{}]", personId, exception);
            throw new PersonDetailException("Exception occurred while deleting person");
        }
    }

    @Override
    public PersonResponse addHobbyToPerson(Long personId, Long hobbyId) {
        PersonResponse personResponse;
        try {
            Optional<PersonEntity> personEntity = personDetailRepository.findById(personId);

            if (personEntity.isEmpty()) {
                logger.error("Person [{}] not found", personId);
                throw new NoDataFoundException("Person not found");
            }
            Optional<HobbyEntity> hobbyEntity = hobbyRepository.findById(hobbyId);

            if (hobbyEntity.isEmpty()) {
                logger.error("Hobby [{}] not found", personId);
                throw new NoDataFoundException("Hobby not found");
            }

            hobbyEntity.get().setPersonEntity(personEntity.get());
            hobbyEntity = Optional.of(hobbyRepository.save(hobbyEntity.get()));
            personEntity.get().addHobby(hobbyEntity.get());

            personResponse = buildPersonResponse(personEntity.get());

        } catch (NoDataFoundException noDataFoundException) {
            throw noDataFoundException;
        } catch (Exception exception) {
            logger.error("Exception occurred while adding hobby [{}] of person [{}]",hobbyId,personId,exception);
            throw new PersonDetailException("Exception occurred while adding hobby of person");
        }
        return personResponse;
    }

    @Override
    public PersonResponse removeHobbyToPerson(Long personId, Long hobbyId) {
        PersonResponse personResponse;
        try {
            Optional<PersonEntity> personEntity = personDetailRepository.findById(personId);

            if (personEntity.isEmpty()) {
                logger.error("Person [{}] not found", personId);
                throw new NoDataFoundException("Person not found");
            }
            Optional<HobbyEntity> hobbyEntity = hobbyRepository.findById(hobbyId);

            if (hobbyEntity.isEmpty()) {
                logger.error("Hobby [{}] not found", personId);
                throw new NoDataFoundException("Hobby not found");
            }

            hobbyEntity.get().setPersonEntity(null);
            hobbyEntity = Optional.of(hobbyRepository.save(hobbyEntity.get()));
            personEntity.get().removeHobby(hobbyEntity.get());

            personResponse = buildPersonResponse(personEntity.get());
        } catch (NoDataFoundException noDataFoundException) {
            throw noDataFoundException;
        } catch (Exception exception) {
            logger.error("Exception occurred while removing hobby [{}] of person [{}]",hobbyId,personId,exception);
            throw new PersonDetailException("Exception occurred while removing hobby of person");
        }
        return personResponse;
    }

    private PersonEntity buildPersonEntity(PersonRequest personRequest) {
        return PersonEntity.builder()
                .firstName(personRequest.getFirstName())
                .lastName(personRequest.getLastName())
                .age(personRequest.getAge())
                .favouriteColor(personRequest.getFavouriteColor())
                .build();

    }

    private PersonResponse buildPersonResponse(PersonEntity personEntity) {
        return PersonResponse.builder()
                .personId(personEntity.getPersonId())
                .firstName(personEntity.getFirstName())
                .lastName(personEntity.getLastName())
                .age(personEntity.getAge())
                .favouriteColor(personEntity.getFavouriteColor())
                .hobby(personEntity.getHobby().stream()
                        .map(hobby -> HobbyResponse.builder()
                                .Id(hobby.getId())
                                .hobby(hobby.getHobby())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private void buildPersonResponseList(List<PersonResponse> personResponseList, List<PersonEntity> personEntityList) {

        for (PersonEntity personEntity: personEntityList) {
            logger.info("Hobbies [{}]", personEntity.getHobby());
            personResponseList.add(PersonResponse.builder()
                    .personId(personEntity.getPersonId())
                    .firstName(personEntity.getFirstName())
                    .lastName(personEntity.getLastName())
                    .age(personEntity.getAge())
                    .favouriteColor(personEntity.getFavouriteColor())
                    .hobby(personEntity.getHobby().stream()
                            .map(h -> HobbyResponse.builder()
                                    .Id(h.getId())
                                    .hobby(h.getHobby())
                                    .build())
                            .collect(Collectors.toList()))
                    .build());
        }
    }

    private void buildPersonEntityForUpdate(PersonEntity personEntity, PersonRequest personRequest) {
        personEntity.setFirstName(personRequest.getFirstName());
        personEntity.setLastName(personRequest.getLastName());
        personEntity.setAge(personRequest.getAge());
        personEntity.setFavouriteColor(personRequest.getFavouriteColor());

    }

}
