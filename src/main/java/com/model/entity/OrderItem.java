package com.model.entity;
import com.model.enums.ProductType;
import com.model.enums.ProgressStatus;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
public class OrderItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private Product product;

    @Enumerated(EnumType.STRING)
    //@NaturalId
    @Column(length = 60)
    private ProgressStatus orderStatus;

    @ManyToOne(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private  Bill bill;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private ProductType productType;

    private String commentaires;

    private double prix;
    //quand le plat doit etre pret
    private LocalDateTime delaiDePreparation ;

    @OneToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private List<Option> option;

    private int numeroTable;

    private Date tempsDePreparation;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProgressStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(ProgressStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public LocalDateTime getDelaiDePreparation() {
        return delaiDePreparation;
    }

    public void setDelaiDePreparation(LocalDateTime delaiDePreparation) {
        this.delaiDePreparation = delaiDePreparation;
    }

    public List<Option> getOption() {
        return option;
    }

    public void setOption(List<Option> option) {
        this.option = option;
    }

    public int getNumeroTable() {
        return numeroTable;
    }

    public void setNumeroTable(int numeroTable) {
        this.numeroTable = numeroTable;
    }

    public Date getTempsDePreparation() {
        return tempsDePreparation;
    }

    public void setTempsDePreparation(Date tempsDePreparation) {
        this.tempsDePreparation = tempsDePreparation;
    }
}


