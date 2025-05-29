package com.internshiptt.client;

import com.internshiptt.entity.Models.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "student",url = "http://localhost:8081")
public interface StudentClient {


        @GetMapping("/student/{id}")
        Student getStudentById(@PathVariable int id);
        @PostMapping("/student/login")
        Student studentLogin(@RequestBody Map<String, String> credentials);
        @PostMapping("/student/register")
        void register(@RequestBody Student student);
}
