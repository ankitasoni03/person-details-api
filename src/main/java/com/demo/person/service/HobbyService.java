package com.demo.person.service;

import com.demo.person.model.request.HobbyRequest;
import com.demo.person.model.response.HobbyResponse;

import java.util.List;

public interface HobbyService {
    List<HobbyResponse> getHobbies();
    HobbyResponse getHobby(Long id);
    HobbyResponse saveHobby(HobbyRequest hobby);
    HobbyResponse updateHobby(Long id, HobbyRequest hobby);
    void deleteHobby(Long id);
}
