package com.internshiptt.client;

import com.internshiptt.entity.Models.Hr;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "hr",url = "http://localhost:8083") // This should match the name registered in Eureka
public interface HrClient {

    @GetMapping("/hr/validate/{hrId}")
    boolean isValidHr(@PathVariable("hrId") Integer hrId);


    @PostMapping("/hr/login")
    Hr hrLogin(@RequestBody Map<String, String> credentials);


@PostMapping("/hr/register")
void register(@RequestBody Hr hr);
    }


