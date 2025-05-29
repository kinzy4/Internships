package com.internshiptt.student.Service;

import com.internshiptt.entity.Models.Student;
import com.internshiptt.student.Repo.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepo;

    public StudentService(StudentRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    public Student getStudentById(int id) {
        Optional<Student> optionalStudent = studentRepo.findById(id);


            return optionalStudent.get();

    }
//-----------------(Register)-----------------------------------------------------
public void registerAsStudent(Student st) {
    if (studentRepo.findByEmail(st.getEmail()).isPresent()) {
        throw new IllegalArgumentException("Email already in use");
    }
    studentRepo.save(st);
}
 //------------------------------------------------------------------------------
 //-----------------(UpdateProfile)-----------------------------------------------------
    public String updateProfile(Student student, int id) {
        Optional<Student> optionalStudent = studentRepo.findById(id);

        if (optionalStudent.isPresent()) {
            Student existing = optionalStudent.get();

            existing.setName(student.getName());
            existing.setEmail(student.getEmail());
            existing.setPassword(student.getPassword());
            existing.setAddress(student.getAddress());
            existing.setAge(student.getAge());
            existing.setType("student");
            existing.setUniversity(student.getUniversity());
            existing.setCollege(student.getCollege());
            existing.setMajor(student.getMajor());
            existing.setLevel(student.getLevel());
            existing.setGpa(student.getGpa());

            studentRepo.save(existing);
            return "Profile updated successfully!";
        } else {
            return "Student not found!";
        }
    }
    //------------------------------------------------------------------------------
    //-----------------(Login)-----------------------------------------------------

    public Student login(String email, String rawPassword) {
        Optional<Student> optionalStudent = studentRepo.findByEmail(email);

        if (optionalStudent.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student Not Found");
        }

        Student student = optionalStudent.get();

        if (!rawPassword.equals(student.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password Incorrect");
        }

        return student;
    }


}
