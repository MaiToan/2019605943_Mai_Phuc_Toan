package com.datn.maiphuctoandatn.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @OneToMany(mappedBy = "AuthorProduct")
    List<ProductAuthor> productAuthors;

    @Column(name = "name", nullable = false, length = 50)
    public String Name ;

    @Column(name = "address", nullable = false)
    public String Address ;

    @Column(name = "avatar", nullable = false)
    public String Avatar ;

    @Column(name = "description", length = 1000)
    public String Description ;

    @Column(name = "loevm")
    public String LOEVM ;

    public String createdAtFormat;

    @CreationTimestamp
    @Column(name = "created_at")
    public Timestamp Created_at;

    @UpdateTimestamp
    @Column(name = "updated_at")
    public Timestamp Updated_at;

    @Column(name = "email")
    public String Email ;
}