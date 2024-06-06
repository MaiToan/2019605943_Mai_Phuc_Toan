package com.datn.maiphuctoandatn.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "OrderOrderDetail")
    List<OrderDetail> orderDetails;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user_order;

    @Column(name = "name", nullable = false, length = 30)
    public String Name;

    @Column(name = "country", nullable = false)
    public String Country;

    @Column(name = "payment_method", nullable = false)
    public Integer PaymentMethod;

    @Column(name = "city", nullable = false)
    public String City;

    @Column(name = "district", nullable = false)
    public String District;

    @Column(name = "address", nullable = false,  length = 100)
    public String Address;

    @Column(name = "phone", nullable = false, length = 10)
    public String Phone;

    @Column(name = "status", nullable = false)
    public Integer status;

    @Column(name = "discount", nullable = false)
    public Double DisCount;

    @CreationTimestamp
    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm:ss")
    public Timestamp OrderDate;

    public String OrderDateFormat;

    @Column(name = "note", length = 200)
    public String Note;

    @CreationTimestamp
    @Column(name = "created_at")
    public Timestamp Created_at;

    @UpdateTimestamp
    @Column(name = "updated_at")
    public Timestamp Updated_at;

}
