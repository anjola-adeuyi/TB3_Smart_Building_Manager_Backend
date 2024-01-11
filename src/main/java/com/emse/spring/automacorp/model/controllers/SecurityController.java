package com.emse.spring.automacorp.model.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/admin/users")
public class SecurityController {
    public record User(String username) {
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/me")
    public User findUserName(@AuthenticationPrincipal UserDetails userDetails) {
        return new User(userDetails.getUsername());
    }
}