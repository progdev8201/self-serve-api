package com.service;


import com.model.dto.JwtResponse;
import com.model.dto.LoginForm;
import com.model.dto.SignUpForm;
import com.model.entity.*;
import com.model.enums.RoleName;
import com.repository.GuestRepository;
import com.repository.RoleRepository;
import com.security.jwt.JwtProvider;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthentificationService {
    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    GuestRepository guestRepository;

    @Autowired
    RoleRepository roleRepository;

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
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    public ResponseEntity<String> registerUser(SignUpForm signUpForm) {
        //make sure email doesnt already exist
        if (guestRepository.existsByUsername(signUpForm.getUsername())) {
            return new ResponseEntity<String>("Fail -> Email is already in use!", HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        Guest user = new Guest(signUpForm.getUsername(), encoder.encode(signUpForm.getPassword()),new TreeSet<>());

        // TRANSFERING ACCOUNT ROLES STRING TO ENUM THEN MAKE SURE ITS IN DATABASE THEN ADD TO USER
        Set<String> requestRolesArr = Collections.singleton(signUpForm.getRole());
        Set<Role> userRoles = new HashSet<Role>();

        ResponseEntity<String> roleStr = validateRoleThenAddToUser(requestRolesArr, userRoles);
        if (roleStr != null) return roleStr;


        //create entity based on roles
        if (createEntityBasedOnRoles(user, userRoles,signUpForm))return ResponseEntity.ok().body("User registered successfully!");
        else return new ResponseEntity<String> ("Fail -> couldn't create User",HttpStatus.BAD_REQUEST);
    }


    //PRIVATE METHODS

    private ResponseEntity<String> validateRoleThenAddToUser(Set<String> requestRolesArr, Set<Role> userRoles) {
        if (requestRolesArr != null) {
            for (String roleStr : requestRolesArr) {
                Optional<RoleName> roleName = lookupRoleNameByName(roleStr);

                //if role is present in the database add it to user
                if (roleName.isPresent()) {
                    Optional<Role> role = roleRepository.findByName(roleName.get());

                    if (role.isPresent())
                        userRoles.add(role.get());
                    else
                        return new ResponseEntity<String>("Fail -> '" + roleStr + "' is not within the role repository", HttpStatus.BAD_REQUEST);

                } else
                    return new ResponseEntity<String>("Fail -> '" + roleStr + "' is not within the possible roles (RoleName enum)", HttpStatus.BAD_REQUEST);
            }
        } else
            return new ResponseEntity<String>("Fail -> Key 'roles' is not present (null) make sure the key exist", HttpStatus.BAD_REQUEST);
        return null;
    }

    private boolean createEntityBasedOnRoles(Guest user, Set<Role> userRoles,SignUpForm signUpForm) {
        user.setRoles(userRoles);
        Iterator<Role> rolesItr = userRoles.iterator();
        while (rolesItr.hasNext()) {

            switch (rolesItr.next().getName()) {
                case ROLE_CLIENT:
                    guestRepository.save(new Client(user,signUpForm.getTelephone()));
                    // send email
                    LOGGER.info("I created a new client");
                    return true;

                case ROLE_DELIVERYMAN:
                    guestRepository.save(new Owner(user));
                    LOGGER.info("I create a new owner");
                    return true;

                case ROLE_ADMIN:
                    guestRepository.save(new Employee(user));
                    LOGGER.info("I create a new employee");
                    return true;

                default:
                    continue;
            }

        }
        return false;
    }

}
