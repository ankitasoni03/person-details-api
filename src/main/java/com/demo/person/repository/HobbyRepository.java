package com.demo.person.repository;

import com.demo.person.entity.HobbyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HobbyRepository extends JpaRepository<HobbyEntity, Long> {
    //public List<HobbyEntity> findByPersonEntity(Long id);
}
