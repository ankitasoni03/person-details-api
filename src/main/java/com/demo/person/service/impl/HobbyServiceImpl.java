package com.demo.person.service.impl;

import com.demo.person.entity.HobbyEntity;
import com.demo.person.exception.NoDataFoundException;
import com.demo.person.exception.PersonDetailException;
import com.demo.person.model.request.HobbyRequest;
import com.demo.person.model.response.HobbyResponse;
import com.demo.person.repository.HobbyRepository;
import com.demo.person.service.HobbyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HobbyServiceImpl implements HobbyService {
    private final Logger logger = LoggerFactory.getLogger(HobbyServiceImpl.class);

    @Autowired
    HobbyRepository hobbyRepository;

    @Override
    public List<HobbyResponse> getHobbies() {
        List<HobbyResponse> hobbies = new ArrayList<>();
        List<HobbyEntity> hobbyEntity;
        try {
            hobbyEntity = hobbyRepository.findAll();
        } catch (Exception exception) {
            logger.error("Exception occurred while fetching hobbies list", exception);
            throw new PersonDetailException("Exception occurred while fetching hobbies list");
        }

        for (HobbyEntity hobby: hobbyEntity) {
            hobbies.add(HobbyResponse.builder().Id(hobby.getId())
                    .hobby(hobby.getHobby())
                    .build());
        }

        return hobbies;
    }

    @Override
    public HobbyResponse getHobby(Long id) {
        Optional<HobbyEntity> hobbyEntity = Optional.empty();
        try {
            hobbyEntity = hobbyRepository.findById(id);
        } catch (Exception exception) {
            logger.error("Exception occurred while looking for hobby [{}]", id, exception);
            throw new PersonDetailException("Exception occurred while looking for hobby");
        }

        if (hobbyEntity.isEmpty()) {
            logger.error("Hobby [{}] not found", id);
            throw new NoDataFoundException("Hobby cannot be found");
        }

        return HobbyResponse.builder().Id(hobbyEntity.get().getId())
                .hobby(hobbyEntity.get().getHobby())
                .build();
    }

    @Override
    public HobbyResponse saveHobby(HobbyRequest hobbyRequest) {
        HobbyResponse hobbyResponse;
        try {
            HobbyEntity hobbyEntity = hobbyRepository.save(HobbyEntity.builder().hobby(hobbyRequest.getHobby()).build());
            hobbyResponse = HobbyResponse.builder().Id(hobbyEntity.getId()).hobby(hobbyRequest.getHobby()).build();
        } catch (Exception exception) {
            logger.error("Exception occurred while saving hobby", exception);
            throw new PersonDetailException("Exception occurred while saving hobby");
        }

        return hobbyResponse;
    }

    @Override
    public HobbyResponse updateHobby(Long id, HobbyRequest hobbyRequest) {
        HobbyResponse hobbyResponse;
        try {
            Optional<HobbyEntity> hobbyEntity = hobbyRepository.findById(id);
            if (hobbyEntity.isEmpty()) {
                logger.error("Hobby [{}] cannot be found", id);
                throw new NoDataFoundException("Hobby not found");
            }

            hobbyEntity.get().setHobby(hobbyRequest.getHobby());
            HobbyEntity updatedHobby = hobbyRepository.save(hobbyEntity.get());
            hobbyResponse = HobbyResponse.builder().hobby(updatedHobby.getHobby()).build();
        } catch (NoDataFoundException noDataFoundException) {
            throw noDataFoundException;
        } catch (Exception exception) {
            logger.error("Exception occurred while updating hobby [{}]", id, exception);
            throw new PersonDetailException("Exception occurred while updating hobby");
        }

        return hobbyResponse;
    }

    @Override
    public void deleteHobby(Long id) {
        try {
            Optional<HobbyEntity> hobbyEntity =  hobbyRepository.findById(id);

            if (hobbyEntity.isEmpty()) {
                logger.error("Hobby [{}] cannot be found", id);
                throw new NoDataFoundException("Hobby not found");
            }
            hobbyRepository.delete(hobbyEntity.get());

        } catch (NoDataFoundException noDataFoundException) {
            throw noDataFoundException;
        } catch (Exception exception) {
            logger.error("Exception occurred while deleting hobby [{}]", id, exception);
            throw new PersonDetailException("Exception occurred while deleting hobby");
        }
    }
}
