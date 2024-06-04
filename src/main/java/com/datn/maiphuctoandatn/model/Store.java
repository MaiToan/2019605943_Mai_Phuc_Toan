package com.datn.maiphuctoandatn.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(name = "name", nullable = false)
    public String Name ;

    @Column(name = "logo")
    public String Logo ;

    @Column (name = "image")
    public String Image ;

    @Column (name = "phone", length = 11)
    public String Phone ;

    @Column (name = "email")
    public String Email ;

    @Column (name = "address")
    public String Address ;

    @Column (name = "description", length = 500)
    public String Description ;

    @CreationTimestamp
    @Column(name = "created_at")
    public Timestamp Created_at;

    @UpdateTimestamp
    @Column(name = "updated_at")
    public Timestamp Updated_at;
}
