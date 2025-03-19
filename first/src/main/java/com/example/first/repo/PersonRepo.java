package com.example.first.repo;

import com.example.first.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepo extends JpaRepository<Person, Long> {
    boolean existsByPeselNumber(String peselNumber);
}
