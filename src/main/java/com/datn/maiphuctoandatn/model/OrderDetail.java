package com.datn.maiphuctoandatn.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OrderDetail {

    @EmbeddedId
    OrderDetailKey id;

    @ManyToOne
    @MapsId("OrderID")
    @JoinColumn(name = "order_id")
    Order OrderOrderDetail;

    @ManyToOne
    @MapsId("ProductID")
    @JoinColumn(name = "product_id")
    Product ProductOrder;

    @Column(name = "quantity", nullable = false)
    public Integer Quantity ;

    @Column(name = "unit_price", nullable = false)
    public Double UnitPrice ;
}
