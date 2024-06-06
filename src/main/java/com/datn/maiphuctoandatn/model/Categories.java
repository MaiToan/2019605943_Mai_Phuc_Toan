package com.datn.maiphuctoandatn.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "categories")
    List<Product> productList ;

    @Column(name = "name", nullable = false, length = 50)
    public String Name ;

    @Column(name = "image", nullable = false)
    public String Avatar ;

    @Column(name = "description", length = 1000)
    public String Description ;

    public String createdAtFormat;

    @Column(name = "loevm", length = 1)
    public String LOEVM ;

    @CreationTimestamp
    @Column(name = "created_at")
    public Timestamp Created_at;

    @UpdateTimestamp
    @Column(name = "updated_at")
    public Timestamp Updated_at;

}
