package com.internshiptt.application.Repo;

import com.internshiptt.entity.Models.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application  ,Integer> {

    List<Application> findByStudentId(int studentId); // Will map to st_id



    List<Application> findByStudentIdAndStatus(int studentId,
                                                     Application.Status status);


    List<Application> findByInternshipId(int internshipId);
}
