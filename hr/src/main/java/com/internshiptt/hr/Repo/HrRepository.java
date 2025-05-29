package com.internshiptt.hr.Repo;

import com.internshiptt.entity.Models.Hr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HrRepository extends JpaRepository<Hr, Integer> {

    boolean existsByHrId(Integer hrId);
    Hr findByEmail(String email);

}
