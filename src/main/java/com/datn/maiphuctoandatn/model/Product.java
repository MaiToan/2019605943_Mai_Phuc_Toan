package com.datn.maiphuctoandatn.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @OneToMany(mappedBy = "ProductAuthor")
    List<ProductAuthor> productAuthors;

    @ManyToMany
    @JoinTable(
            name = "favorite",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> userList;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Categories categories;

    public String getNameCategory() {
        if (categories != null)
            return categories.Name;
        return "";
    }

    @OneToMany(mappedBy = "product")
    List<Comment> commentList;

    @OneToMany(mappedBy = "ProductOrder")
    List<OrderDetail> orderDetailProduct;

    @Column(name = "name", nullable = false, length = 50)
    public String Name;

    @Column(name = "price", nullable = false)
    public Double Price;

    @Column(name = "sale")
    public Double Sale;

    @Column(name = "image", nullable = false)
    public String Image;

    @Column(name = "loevm", length = 1)
    public String Loevm;

    @Column(name = "description", length = 1000)
    public String Description;

    @Column(name = "product_number")
    public Integer ProductNumber;

    @Column(name = "product_sold")
    public Integer ProductSold;

    public String CreateFormatDate;
    public String UnitPrice ;

    @CreationTimestamp
    @Column(name = "created_at")
    public Timestamp Created_at;

    @UpdateTimestamp
    @Column(name = "updated_at")
    public Timestamp Updated_at;


//    @Override
//    public int compareTo(Product product) {
//        return Integer.compare(this.Price, product.Price);
//    }
}
