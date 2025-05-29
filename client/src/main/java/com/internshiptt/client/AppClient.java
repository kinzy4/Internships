package com.internshiptt.client;

import com.internshiptt.entity.Models.Application;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "application",url = "http://localhost:8084") // This should match the service name in Eureka
public interface AppClient {

    @PostMapping("/applications/submit")  // Replace with the correct endpoint for submitting applications
    void submitApplication(@RequestBody Application app);

    @PutMapping("/applications/updatestatus/{id}")
    Application updateStatus(@PathVariable int id, @RequestBody Application.Status status);

//    @GetMapping("/applications/student/studentId")
//    public ResponseEntity<List<Application>> getApplicationsByStudent(
//            @PathVariable int studentId,
//            @RequestParam(required = false) Application.Status status);
@GetMapping("/applications/student/{studentId}")
List<Application> getApplicationsByStudent(
        @PathVariable int studentId,
        @RequestParam(value = "status", required = false) Application.Status status);
    @GetMapping("/applications/{id}")
    Application getApplicationById(@PathVariable("id") int id);
}





