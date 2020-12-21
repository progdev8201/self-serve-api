package com.service.validator;

import com.model.entity.Employer;
import com.model.entity.Owner;
import com.repository.EmployerRepository;
import com.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RestaurantOwnerShip {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private EmployerRepository employerRepository;

    public boolean hasOwnerRight(Long restauranId){
        Authentication authentication = getAuthentication();

        if (authentication == null)
            return true;

        Long ownerId =  Long.parseLong((String) getAuthentication().getPrincipal());

        Owner owner = ownerRepository.findById(ownerId).get();

        return owner.getRestaurantList().stream().anyMatch(restaurant -> restauranId.equals(restaurant.getId()));
    }

    public boolean hasCookWaiterRight(Long restaurantId){
        Authentication authentication = getAuthentication();

        if (authentication == null)
            return true;

        Long employerId =  Long.parseLong((String) getAuthentication().getPrincipal());

        Employer employer = employerRepository.findById(employerId).get();

        return employer.getRestaurant().getId().equals(restaurantId);
    }

    private Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
