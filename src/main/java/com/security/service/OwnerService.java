package com.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service(value = "ownerService")
public class OwnerService {

    public boolean isRecordOwner(Authentication authentication, String id) {
        return authentication.getName().equals(id);
    }
}
