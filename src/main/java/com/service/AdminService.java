package com.service;

import com.mapping.OwnerToOwnerDTO;
import com.model.dto.OwnerDTO;
import com.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    OwnerRepository ownerRepository;

    public List<OwnerDTO> getAllOwners(){
        List<OwnerDTO> ownerDTOS = new ArrayList<>();
        ownerRepository.findAll().forEach(owner -> {
            ownerDTOS.add(OwnerToOwnerDTO.instance.convert(owner));
        });
        return ownerDTOS;
    }
}
