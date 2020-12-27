package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.model.dto.BillDTO;
import com.model.dto.JwtResponse;
import com.model.dto.LoginForm;
import com.model.dto.ProductDTO;
import com.model.dto.requests.FindBillBetweenDateRequestDTO;
import com.model.entity.Bill;
import com.model.enums.BillStatus;
import com.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class BillController {

    @Autowired
    private ClientService clientService;

    //
    // GetMapping
    //

    @PreAuthorize("hasAnyAuthority('ROLE_GUEST','ROLE_CLIENT')")
    @GetMapping("/billStatus/{billId}")
    public BillStatus findBillStatus(@PathVariable final Long billId){
        return clientService.findBillStatus(billId);
    }

    //
    // PostMapping
    //

    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    @PostMapping("/getPaidBillsBetweenDates")
    public List<BillDTO> findAllPaidBillsByRestaurantBetweenDates(@RequestBody FindBillBetweenDateRequestDTO findBillBetweenDateRequestDTO) throws Exception {
        return clientService.findAllPaidBillsByRestaurantBetweenDates(findBillBetweenDateRequestDTO);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GUEST','ROLE_CLIENT')")
    @PostMapping("/makeOrder")
    public ResponseEntity<BillDTO> makeOrder(@RequestBody Map<String, String> json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        BillDTO billDTO = objectMapper.readValue(json.get("billDTO"),BillDTO.class);
        Long restaurentTableId = objectMapper.readValue(json.get("restaurentTableId"),Long.class);
        ProductDTO productDTO = objectMapper.readValue(json.get("productDTO"), ProductDTO.class);
        String commentaire =json.get("commentaire");
        return ResponseEntity.ok(clientService.makeOrder(productDTO,json.get("guestUsername"),billDTO.getId(),restaurentTableId,commentaire));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GUEST','ROLE_CLIENT')")
    @PostMapping("/initBill")
    public ResponseEntity<BillDTO> initBill() {
        return ResponseEntity.ok(clientService.initBill());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GUEST','ROLE_CLIENT','ROLE_OWNER')")
    @PostMapping("/makePayment")
    public ResponseEntity<Boolean> makePayment(@RequestBody Map<String, String> json) throws JsonProcessingException {
        Long billId = new ObjectMapper().readValue(json.get("billId"), Long.class);
        return ResponseEntity.ok(clientService.makePayment(billId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GUEST','ROLE_CLIENT')")
    @PostMapping("/getBill")
    public ResponseEntity<BillDTO> getBill(@RequestBody Map<String, String> json) throws JsonProcessingException {
        Long billId = new ObjectMapper().readValue(json.get("billId"), Long.class);
        return ResponseEntity.ok(clientService.fetchBill(billId));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_GUEST','ROLE_CLIENT')")
    @PutMapping("/updateBills")
    public ResponseEntity<BillDTO> updateBills(@RequestBody BillDTO billDTO) throws JsonProcessingException {
        return ResponseEntity.ok(clientService.updateBill(billDTO));
    }
}
