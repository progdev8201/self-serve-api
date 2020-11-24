package com.model.dto.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.model.dto.BillDTO;
import com.model.dto.ProductDTO;
import com.model.entity.Bill;

import java.util.Map;

public class MakeOrderRequestDTO {
    private BillDTO billDTO;

    private Long restaurentTableId;

    private ProductDTO productDTO;

    private String commentaire;

    private String guestUsername;

    public MakeOrderRequestDTO(Map<String, String> json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        this.billDTO = objectMapper.readValue(json.get("billDTO"),BillDTO.class);
        this.restaurentTableId = objectMapper.readValue(json.get("restaurentTableId"),Long.class);
        this.productDTO = objectMapper.readValue(json.get("productDTO"), ProductDTO.class);
        this.commentaire =json.get("commentaire");
    }

    public MakeOrderRequestDTO() {
    }

    public BillDTO getBillDTO() {
        return billDTO;
    }

    public void setBillDTO(BillDTO billDTO) {
        this.billDTO = billDTO;
    }

    public Long getRestaurentTableId() {
        return restaurentTableId;
    }

    public void setRestaurentTableId(Long restaurentTableId) {
        this.restaurentTableId = restaurentTableId;
    }

    public ProductDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(ProductDTO productDTO) {
        this.productDTO = productDTO;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getGuestUsername() {
        return guestUsername;
    }

    public void setGuestUsername(String guestUsername) {
        this.guestUsername = guestUsername;
    }
}
