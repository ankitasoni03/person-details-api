package com.demo.person.repository;

import com.demo.person.entity.HobbyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HobbyRepository extends JpaRepository<HobbyEntity, Long> {
}
