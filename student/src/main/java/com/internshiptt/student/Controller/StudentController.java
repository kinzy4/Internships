package com.internshiptt.student.Controller;

import com.internshiptt.client.*;  // Import FeignClient for Application Service
import com.internshiptt.client.InternshipClient;  // Import FeignClient for Internship Service

import com.internshiptt.entity.Models.*;
import com.internshiptt.student.Service.StudentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

import static com.internshiptt.entity.Models.Application.Status.Pending;


@RestController
@RequestMapping("/student")

public class StudentController {

    private final StudentService studentService;
    private final com.internshiptt.client.AppClient appClient;  // Feign client for ApplicationService
    private final InternshipClient internshipClient;  // Feign client for InternshipService

    // Inject FeignClients
    public StudentController(StudentService studentService, com.internshiptt.client.AppClient appClient, InternshipClient internshipClient) {
        this.studentService = studentService;
        this.appClient = appClient;
        this.internshipClient = internshipClient;
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Student student) {
        try {
            studentService.registerAsStudent(student);
            return ResponseEntity.ok("Registered Successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }
    }

    @PutMapping("/updateprofile/{id}")
    public ResponseEntity<String> update_profile(@PathVariable int id, @RequestBody Student student) {
        String result = studentService.updateProfile(student, id);
        if (result.equals("Profile updated successfully!")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }

@PostMapping("/login")
public ResponseEntity<Student> login(
        HttpServletRequest request,
        @RequestBody Map<String, String> credentials
) {
    String email = credentials.get("email");
    String password = credentials.get("password");

    Student student = studentService.login(email,password);
    if (student == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
    }

    if (!password.equals(student.getPassword())) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password Incorrect");
    }

    HttpSession session = request.getSession(true);
    session.setAttribute("userEmail", email);

    return ResponseEntity.ok(student);
}

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        request.getSession().invalidate(); // This logs the user out
        return ResponseEntity.ok("Logged out successfully");
    }




    @PostMapping("/submitapplication")
    public ResponseEntity<String> submit_app(@RequestBody Application app) {
        app.setStatus(Pending);
        // Directly call ApplicationService using FeignClient
        appClient.submitApplication(app);
        return ResponseEntity.ok("Application submitted successfully!");
    }
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }
    @GetMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public List<Internship> getAllInternships() {

        return internshipClient.getAllInternships();
    }

    @GetMapping("/applications/student/{studentId}")

    public ResponseEntity<List<Application>> getStudentApplications(
            @PathVariable int studentId,
            @RequestParam(required = false) Application.Status status) {

        List<Application> applications = appClient.getApplicationsByStudent(studentId, status);
        return ResponseEntity.ok(applications);
    }
}

