package com.demo.person.repository;

import com.demo.person.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonDetailRepository extends JpaRepository<PersonEntity, Long> {
}
