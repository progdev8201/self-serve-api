package com.service;

import com.mapping.OwnerToOwnerDTO;
import com.model.dto.JwtResponse;
import com.model.dto.LoginForm;
import com.model.dto.OwnerDTO;
import com.model.dto.SignUpForm;
import com.model.entity.*;
import com.model.enums.RoleName;
import com.repository.AdminRepository;
import com.repository.OwnerRepository;
import com.security.jwt.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthentificationService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;


    private static final Logger LOGGER = LoggerFactory.getLogger(AuthentificationService.class);

    private static final Map<String, RoleName> nameIndex = new HashMap<>(RoleName.values().length);

    // Static initializer
    static {
        for (RoleName roleName : RoleName.values()) {
            nameIndex.put(roleName.name(), roleName);
        }
    }

    // Method to lookup Role name by name returning an optional so we dont need a try catch
    private static Optional<RoleName> lookupRoleNameByName(String name) {
        return Optional.ofNullable(nameIndex.get("ROLE_" + name.toUpperCase()));
    }

    public ResponseEntity<JwtResponse> authenticateUser(LoginForm loginForm) {

        final Optional<Admin> user = adminRepository.findByUsername(loginForm.getUsername());

        if (user.isPresent() && encoder.matches(loginForm.getPassword(), user.get().getPassword())) {

            final String token = jwtProvider.generate(user.get());

            return ResponseEntity.ok(new JwtResponse(token));
        }

        return null;
    }

    public ResponseEntity<OwnerDTO> fetchOwner(OwnerDTO ownerDTO) {
        Owner owner = ownerRepository.findByUsername(ownerDTO.getUsername()).get();
        return ResponseEntity.ok(OwnerToOwnerDTO.instance.convert(owner));
    }

    public ResponseEntity<String> registerUser(SignUpForm signUpForm) {
        //make sure email doesnt already exist
        if (adminRepository.existsByUsername(signUpForm.getUsername())) {
            return new ResponseEntity<String>("Fail -> Email is already in use!", HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        Guest user = new Guest(signUpForm.getUsername(), encoder.encode(signUpForm.getPassword()), signUpForm.getRole());

        //create entity based on roles
        if (createEntityBasedOnRoles(signUpForm))
            return ResponseEntity.ok().body("User registered successfully!");
        else return new ResponseEntity<String>("Fail -> couldn't create User", HttpStatus.BAD_REQUEST);
    }


    //PRIVATE METHODS

    private boolean createEntityBasedOnRoles(SignUpForm user) {
        Optional<RoleName> roleName = lookupRoleNameByName(user.getRole());

        if (!roleName.isPresent()) {
            return false;
        }

        switch (roleName.get()) {
            case ROLE_CLIENT:
                adminRepository.save(new Client(user.getUsername(), encoder.encode(user.getPassword()), user.getTelephone(), RoleName.ROLE_CLIENT.toString()));
                // send email
                LOGGER.info("I created a new client");
                return true;

            case ROLE_OWNER:
                adminRepository.save(new Owner(user.getUsername(), encoder.encode(user.getPassword()), RoleName.ROLE_OWNER.toString()));
                LOGGER.info("I created a new owner");
                return true;

            case ROLE_COOK:
                adminRepository.save(new Cook(user.getUsername(), encoder.encode(user.getPassword()), RoleName.ROLE_COOK.toString()));
                LOGGER.info("I created a new cook");
                return true;

            case ROLE_WAITER:
                adminRepository.save(new Waiter(user.getUsername(), encoder.encode(user.getPassword()), RoleName.ROLE_WAITER.toString()));
                LOGGER.info("I created a new waiter");
                return true;
            case ROLE_ADMIN:
                adminRepository.save(new Admin(user.getUsername(), encoder.encode(user.getPassword()), RoleName.ROLE_ADMIN.toString()));
                LOGGER.info("I created a new Admin");
                return true;

            case ROLE_GUEST:
                adminRepository.save(new Guest(user.getUsername(), encoder.encode(user.getPassword()), RoleName.ROLE_GUEST.toString()));
                LOGGER.info("I created a new Guest");
                return true;

            default:
                break;
        }

        return false;
    }

}
