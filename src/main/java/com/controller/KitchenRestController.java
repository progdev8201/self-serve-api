package com.controller;

import com.model.enums.ProgressStatus;
import com.model.entity.Request;
import com.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("rest/kitchen")
public class KitchenRestController {

    @Autowired
    RequestRepository requestRepository;

    //ALLOW SERVER AND COOK TO LIST REQUEST
    @GetMapping("/request-all")
    public List<Request> findAll(){
        List<Request> requestList = new ArrayList<>();
         requestRepository.findAll().forEach(r -> {
            if (r.getProgressStatus() != ProgressStatus.READY)
                requestList.add(r);
         });

         return  requestList;
    }

    //ALLOW COOK TO LIST ORDERS



    //ALLOW COOK TO MODIFY ORDER TIME OR END ORDER

}
