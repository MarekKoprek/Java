package com.example.first.repo;

import com.example.first.model.Gender;
import com.example.first.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PersonRepo extends JpaRepository<Person, Long> {
    boolean existsByPeselNumber(String peselNumber);
    Long countAllByGender(Gender gender);
    Long countAllByGenderAndBirthDateBefore(Gender gender, LocalDate birthDate);

    @Query("select distinct firstName from Person")
    List<String> findDistinctFirstName();
}
