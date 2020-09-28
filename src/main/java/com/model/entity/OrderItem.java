package com.model.entity;
import com.model.enums.ProductType;
import com.model.enums.ProgressStatus;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Product product;

    @Enumerated(EnumType.STRING)
    //@NaturalId
    @Column(length = 60)
    private ProgressStatus orderStatus;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private  Bill bill;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private ProductType productType;

    private String commentaires;

    private double prix;
    //quand le plat doit etre pret
    private LocalDateTime delaiDePreparation ;

    @OneToMany(cascade =CascadeType.PERSIST)
    private List<Option> option;

    private int numeroTable;

    private Date tempsDePreparation;


    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public int getNumeroTable() {
        return numeroTable;
    }

    public void setNumeroTable(int numeroTable) {
        this.numeroTable = numeroTable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public Date getTempsDePreparation() {
        return tempsDePreparation;
    }

    public void setTempsDePreparation(Date tempsDePreparation) {
        this.tempsDePreparation = tempsDePreparation;
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

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", product=" + product +
                ", orderStatus=" + orderStatus +
                ", prix=" + prix +
                ", delaiDePreparation=" + delaiDePreparation +
                ", option=" + option +
                '}';
    }
}


