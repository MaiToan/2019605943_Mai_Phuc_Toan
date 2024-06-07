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
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(name = "title", nullable = false, length = 500)
    public String Title ;

    @Column(name = "image", nullable = false, length = 500)
    public String Image ;

    @Column(name = "content", nullable = false, length = 1000)
    public String Content ;

    @Column(name = "loevm")
    public String LOEVM ;

    public String Created_at_format;


    @CreationTimestamp
    @Column(name = "created_at")
    public Timestamp Created_at;

    @UpdateTimestamp
    @Column(name = "updated_at")
    public Timestamp Updated_at;
}
