package com.demo.person.controller;

import com.demo.person.fixture.PersonDetailFixture;
import com.demo.person.model.request.PersonRequest;
import com.demo.person.model.response.HobbyResponse;
import com.demo.person.model.response.PersonResponse;
import com.demo.person.repository.HobbyRepository;
import com.demo.person.repository.PersonDetailRepository;
import com.demo.person.service.HobbyService;
import com.demo.person.service.PersonDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = PersonDetailController.class)
class PersonDetailControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PersonDetailService personDetailService;

    @MockBean
    HobbyService hobbyService;

    @MockBean
    PersonDetailRepository personDetailRepository;

    @MockBean
    HobbyRepository hobbyRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void getPersonListTest() throws Exception {
        when(personDetailService.getPersonList()).thenReturn(PersonDetailFixture.getPersonList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/persons")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getPersonTest() throws Exception {
        when(personDetailService.getPerson(anyLong())).thenReturn(PersonDetailFixture.getPersonList().get(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/person/{personId}",1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.first_name", Matchers.is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", Matchers.is("29")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void savePersonTest() throws Exception {
        when(personDetailService.savePerson(any(PersonRequest.class)))
                .thenReturn(PersonDetailFixture.getPersonList().get(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(PersonDetailFixture.getPersonRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.first_name", Matchers.is("John")))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void updatePersonTest() throws Exception{
        PersonResponse personResponse = PersonDetailFixture.getPersonList().get(0);
        personResponse.setFavouriteColor("pink");
        PersonRequest personRequest = PersonDetailFixture.getPersonRequest();
        personRequest.setFavouriteColor("pink");

        when(personDetailService.updatePerson(anyLong(), any(PersonRequest.class)))
                .thenReturn(personResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/person/{personId}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(personRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.favourite_color", Matchers.is("pink")))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void deletePersonTest() throws Exception {
        doNothing().when(personDetailService).deletePerson(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/person/{personId}",1)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void addHobbyToPersonTest() throws Exception {
        PersonResponse personResponse = PersonDetailFixture.getPersonList().get(0);
        HobbyResponse hobbyResponse = HobbyResponse.builder().Id(4L).hobby("dancing").build();
        List<HobbyResponse> hobbyList = personResponse.getHobby();
        hobbyList.add(hobbyResponse);
        personResponse.setHobby(hobbyList);

        when(personDetailService.addHobbyToPerson(anyLong(), anyLong()))
                .thenReturn(personResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/person/{personId}/hobby/{hobbyId}",1,2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.hobby", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hobby[2].hobby",Matchers.is("dancing")))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void removeHobbyToPersonTest() throws Exception {
        PersonResponse personResponse = PersonDetailFixture.getPersonList().get(0);
        List<HobbyResponse> hobbyList = personResponse.getHobby();
        hobbyList.remove(0);
        personResponse.setHobby(hobbyList);
        when(personDetailService.removeHobbyToPerson(anyLong(), anyLong()))
                .thenReturn(personResponse);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/person/{personId}/hobby/{hobbyId}",1,2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.hobby", Matchers.hasSize(1)))
                .andDo(MockMvcResultHandlers.print());
    }


}
