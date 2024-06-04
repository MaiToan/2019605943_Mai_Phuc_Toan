package com.datn.maiphuctoandatn.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @OneToMany(mappedBy = "user")
    List<Comment> commentList ;

    @OneToMany(mappedBy = "user_order")
    List<Order> orders_user;

    @ManyToMany(mappedBy = "userList")
    List<Product> productList ;

    @Column(name = "name", nullable = false, length = 30)
    public String Name;

    @Column(name = "email", length = 40)
    public String Email;

    @Column(name = "address", length = 100)
    public String Address;

    @Column(name = "phone", length = 10)
    public String Phone;

    @Column(name = "age")
    public Integer Age;

    @Column(name = "gender")
    public Integer Gender;

    @Column(name = "password", length = 100)
    public String Password;

    @Column(name = "avatar")
    public String Avatar;

    @Column(name = "active")
    public Integer Active;

    @Column(name = "role")
    public String Role;

    @CreationTimestamp
    @Column(name = "created_at")
    public Timestamp Created_at;

    @UpdateTimestamp
    @Column(name = "updated_at")
    public Timestamp Updated_at;
}
