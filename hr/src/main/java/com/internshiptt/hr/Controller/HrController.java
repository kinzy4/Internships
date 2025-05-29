package com.internshiptt.hr.Controller;

import com.internshiptt.client.*;  // FeignClient for Application Service
import com.internshiptt.client.InternshipClient;  // FeignClient for Internship Service

import com.internshiptt.entity.Models.*;
import com.internshiptt.hr.Service.HrService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.List;


@RestController
@RequestMapping("/hr")
public class HrController {

    private final HrService hrService;
    private final com.internshiptt.client.AppClient appClient;  // Feign client for ApplicationService
    private final InternshipClient internshipClient;  // Feign client for InternshipService
    private StudentClient studentClient;
    // Inject FeignClients and services
    public HrController(HrService hrService,StudentClient studentClient, com.internshiptt.client.AppClient appClient, InternshipClient internshipClient) {
        this.hrService = hrService;
        this.appClient = appClient;
        this.internshipClient = internshipClient;
        this.studentClient=studentClient;
    }

    // Endpoint to update HR profile
    @PutMapping("/updateprofile/{id}")
    public ResponseEntity<String> updateProfile(@PathVariable int id, @RequestBody Hr hr) {
        String result = hrService.updateProfile(hr, id);
        if (result.equals("Profile updated successfully!")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }

    // Endpoint to add a new HR
    @PostMapping("/addhr")
    public ResponseEntity<String> addHr(@RequestBody Hr hrr) {
        hrr.setType("hr");
        if(hrr.getCompanyName() == null) {
            hrr.setCompanyName("DEFAULT_COMPANY"); // Temporary default
        }
        hrService.addHr(hrr);
        return ResponseEntity.ok("HR added successfully");
    }

    @PostMapping("login")
    public ResponseEntity<Hr> login(@RequestBody Map<String, String> credentials, HttpServletRequest request) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Hr hr = hrService.hrlogin(email, password);

        if (hr == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        // Optional: store session info
        HttpSession session = request.getSession(true);
        session.setAttribute("userEmail", email);

        return ResponseEntity.ok(hr); // ✅ Now returns Hr object as JSON
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Hr hr) {
        try {
            hrService.register(hr);
            return ResponseEntity.ok("HR Registered Successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        request.getSession().invalidate(); // This logs the user out
        return ResponseEntity.ok("Logged out successfully");
    }
    // Endpoint to add an internship, calls InternshipService via FeignClient
    @PostMapping("/addinternship/{hrid}")  // Changed {id} to {hrid}
    public ResponseEntity<String> addInternship(
            @RequestBody Internship inter,
            @PathVariable String hrid  // Now matches Feign call
    ) {
        internshipClient.addInternship(inter, hrid);

        return ResponseEntity.ok("Internship added successfully");
    }

//    // Endpoint to update internship, calls InternshipService via FeignClient
//    @PutMapping("/updateinternship/{id}")
//    public ResponseEntity<String> updateInternship(@PathVariable int id, @RequestBody Internship inter) {
//        String result = internshipClient.updateInternship(inter, id);  // Calls InternshipService through Feign Client
//        if (result.equals("Internship updated successfully!")) {
//            return ResponseEntity.ok(result);
//        } else {
//            return ResponseEntity.status(404).body(result);
//        }
//    }
@PutMapping("/updateinternship/{id}")
    public ResponseEntity<Internship> updateInternship(
            @PathVariable int id,
            @RequestBody Internship updatedInternship) {

      ResponseEntity < Internship> result = internshipClient.updateInternship(updatedInternship,id);
        return result;
    }


        // Endpoint to update application status
        @PutMapping("/updatestatus/{id}")
        public ResponseEntity<String> updateStatus(@PathVariable int id, @RequestBody Map<String, String> body) {
            try {
                String statusValue = body.get("status");
                Application.Status status = Application.Status.valueOf(statusValue);

                // Update the application status through ApplicationService Feign Client
                Application application = appClient.updateStatus(id, status);

                if (application == null) {
                    return ResponseEntity.status(404).body("Application not found with ID: " + id);
                }

                // Fetch related student and internship details
                Student student = studentClient.getStudentById(application.getStudentId());


                if (student == null ) {
                    return ResponseEntity.status(404).body("Student  not found for this application");
                }

                // Now that you have student and internship details, send appropriate email
                if (status == Application.Status.Accepted) {
                    String studentEmail = student.getEmail();

                    hrService.sendAcceptanceEmail(studentEmail);
                } else if (status == Application.Status.Rejected) {
                    String studentEmail = student.getEmail();

                    hrService.sendRejectionEmail(studentEmail);
                }

                return ResponseEntity.ok("Status updated successfully!");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error updating application status: " + e.getMessage());
            }
        }


    @GetMapping("/validate/{hrId}")
    public ResponseEntity<Boolean> validateHr(@PathVariable Integer hrId) {
        return ResponseEntity.ok(hrService.isValidHr(hrId));
    }

    // Endpoint to get all internships, calls InternshipService via FeignClient
    @GetMapping
    public List<Internship> getAllInternships() {
        return internshipClient.getAllInternships();  // Fetch all internships through Feign Client
    }
}
