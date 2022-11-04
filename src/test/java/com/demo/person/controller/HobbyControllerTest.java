package com.demo.person.controller;

import com.demo.person.fixture.PersonDetailFixture;
import com.demo.person.model.request.HobbyRequest;
import com.demo.person.model.response.HobbyResponse;
import com.demo.person.repository.HobbyRepository;
import com.demo.person.repository.PersonDetailRepository;
import com.demo.person.service.HobbyService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {HobbyController.class})
class HobbyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    HobbyService hobbyService;

    @MockBean
    HobbyRepository hobbyRepository;

    @MockBean
    PersonDetailRepository personDetailRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void getHobbiesTest() throws Exception {
        when(hobbyService.getHobbies()).thenReturn(PersonDetailFixture.getHobbyResponseList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/hobbies")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getHobbyTest() throws Exception {
        when(hobbyService.getHobby(anyLong())).thenReturn(PersonDetailFixture.getHobbyResponseList().get(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/hobby/{hobbyId}",1)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.hobby", Matchers.is("shopping")))
                        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void saveHobbyTest() throws Exception {
        when(hobbyService.saveHobby(any(HobbyRequest.class)))
                .thenReturn(PersonDetailFixture.getHobbyResponseList().get(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/hobby")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(PersonDetailFixture.getHobbyRequest()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.hobby", Matchers.is("shopping")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void updateHobbyTest() throws Exception {
        HobbyResponse hobbyResponse = PersonDetailFixture.getHobbyResponseList().get(0);
        hobbyResponse.setHobby("dancing");
        HobbyRequest hobbyRequest = PersonDetailFixture.getHobbyRequest();
        hobbyRequest.setHobby("dancing");
        when(hobbyService.updateHobby(anyLong(), any(HobbyRequest.class))).thenReturn(hobbyResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/hobby/{hobbyId}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(hobbyRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.hobby", Matchers.is("dancing")))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void deleteHobbyTest() throws Exception {
        doNothing().when(hobbyService).deleteHobby(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/hobby/{hobbyId}",1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
