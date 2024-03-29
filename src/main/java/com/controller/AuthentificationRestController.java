package com.controller;

import com.model.dto.JwtResponse;
import com.model.dto.LoginForm;
import com.model.dto.OwnerDTO;
import com.model.dto.SignUpForm;
import com.model.entity.Owner;
import com.service.AuthentificationService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthentificationRestController {
    @Autowired
    private AuthentificationService authentificationService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginForm loginForm) {
        return authentificationService.authenticateUser(loginForm);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignUpForm signUpForm) {
        return authentificationService.registerUser(signUpForm);
    }

    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    @PostMapping("/fetchOwner")
    public ResponseEntity<OwnerDTO> fetchOwner(@RequestBody OwnerDTO ownerDTO) {
       return authentificationService.fetchOwner(ownerDTO);
    }
}
