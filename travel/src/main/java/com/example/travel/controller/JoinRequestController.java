package com.example.travel.controller;

import com.example.travel.entity.JoinRequest;
import com.example.travel.service.JoinRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/join-requests")
public class JoinRequestController {

    @Autowired
    private JoinRequestService joinRequestService;

    @GetMapping
    public List<JoinRequest> getAllJoinRequests() {
        return joinRequestService.getAllJoinRequests();
    }

    @GetMapping("/{id}")
    public JoinRequest getJoinRequestById(@PathVariable Long id) {
        return joinRequestService.getJoinRequestById(id);
    }

    @PostMapping
    public JoinRequest createJoinRequest(@RequestBody JoinRequest joinRequest) {
        return joinRequestService.createJoinRequest(joinRequest);
    }

    @PostMapping("/{id}/process")
    public JoinRequest processJoinRequest(@PathVariable Long id, @RequestParam boolean approved) {
        return joinRequestService.processJoinRequest(id, approved);
    }

    @DeleteMapping("/{id}")
    public void deleteJoinRequest(@PathVariable Long id) {
        joinRequestService.deleteJoinRequest(id);
    }
}
