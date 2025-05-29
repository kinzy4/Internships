package com.internshiptt.application.Service;

import com.internshiptt.entity.Models.Application;
import com.internshiptt.application.Repo.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppService {

    private final ApplicationRepository applicationRepository;

    public AppService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public void submitApplication(Application app) {
        applicationRepository.save(app);
    }
    //h
    public List<Application> getApplicationsByStudent(int studentId) {
        return applicationRepository.findByStudentId(studentId);
    }
//h
//    public Application updateStatus(int applicationId, Application.Status newStatus) {    {
//    Optional<Application> optionalApp = applicationRepository.findById(applicationId);        if (optionalApp.isPresent()) {
//        Application application = optionalApp.get();            application.setStatus(newStatus);
//        return applicationRepository.save(application);        } else {
//        throw new RuntimeException("Application with ID " + applicationId + " not found");        }
//    }}
    public List<Application> getApplicationsByStudentAndStatus(int studentId, Application.Status status) {
        System.out.println("Fetching applications for studentId: " + studentId);

        return applicationRepository.findByStudentIdAndStatus(studentId, status);
    }
    public Application getApplicationById(int id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
    }

    public Application updateStatus(int applicationId, Application.Status newStatus) {
        Optional<Application> optionalApp = applicationRepository.findById(applicationId);
        if (optionalApp.isPresent()) {
            Application application = optionalApp.get();
            application.setStatus(newStatus);
            return applicationRepository.save(application);
        } else {
            throw new RuntimeException("Application with ID " + applicationId + " not found");
        }
    }
}
