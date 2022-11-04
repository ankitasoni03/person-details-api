package com.demo.person.fixture;

import com.demo.person.entity.HobbyEntity;
import com.demo.person.entity.PersonEntity;
import com.demo.person.model.request.HobbyRequest;
import com.demo.person.model.request.PersonRequest;
import com.demo.person.model.response.HobbyResponse;
import com.demo.person.model.response.PersonResponse;

import java.util.ArrayList;
import java.util.List;

public class PersonDetailFixture {

    public static List<PersonResponse> getPersonList() {
        List<PersonResponse> personResponseList = new ArrayList<>();
        List<HobbyResponse> hobbyResponses = new ArrayList<>();
        List<HobbyResponse> hobbyResponses2 = new ArrayList<>();

        hobbyResponses.add(HobbyResponse.builder().Id(1L).hobby("shopping").build());
        hobbyResponses.add(HobbyResponse.builder().Id(2L).hobby("football").build());
        hobbyResponses2.add(HobbyResponse.builder().Id(3L).hobby("chess").build());

        personResponseList.add(PersonResponse.builder().personId(1L).firstName("John").lastName("Wick").age("29").favouriteColor("red")
                .hobby(hobbyResponses).build());
        personResponseList.add(PersonResponse.builder().personId(2L).firstName("Sarah").lastName("Raven").age("54").favouriteColor("blue")
                .hobby(hobbyResponses2).build());

        return personResponseList;
    }

    public static PersonRequest getPersonRequest() {
        return PersonRequest.builder().firstName("John").lastName("Wick").age("29").favouriteColor("red").build();
    }

    public static List<HobbyResponse> getHobbyResponseList() {
        List<HobbyResponse> hobbyResponseList = new ArrayList<>();
        hobbyResponseList.add(HobbyResponse.builder().Id(1L).hobby("shopping").build());
        hobbyResponseList.add(HobbyResponse.builder().Id(2L).hobby("football").build());

        return hobbyResponseList;
    }

    public static HobbyRequest getHobbyRequest() {
        return HobbyRequest.builder().hobby("shopping").build();
    }

    public static List<PersonEntity> getPersonEntityList() {
        List<PersonEntity> personEntityList = new ArrayList<>();
        List<HobbyEntity> hobbyEntityList = new ArrayList<>();

        hobbyEntityList.add(HobbyEntity.builder().id(1L).hobby("shopping").build());
        hobbyEntityList.add(HobbyEntity.builder().id(2L).hobby("football").build());

        personEntityList.add(PersonEntity.builder().personId(1L).firstName("John").lastName("Wick").age("29").favouriteColor("red")
                .hobby(hobbyEntityList).build());
        personEntityList.add(PersonEntity.builder().personId(2L).firstName("Sarah").lastName("Raven").age("33").favouriteColor("blue").hobby(new ArrayList<>()).build());

        return personEntityList;
    }

    public static List<HobbyEntity> getHobbyEntityList() {
        List<HobbyEntity> hobbyEntityList = new ArrayList<>();
        hobbyEntityList.add(HobbyEntity.builder().id(1L).hobby("shopping").build());
        hobbyEntityList.add(HobbyEntity.builder().id(2L).hobby("football").build());
        hobbyEntityList.add(HobbyEntity.builder().id(3L).hobby("chess").build());

        return hobbyEntityList;
    }
}
