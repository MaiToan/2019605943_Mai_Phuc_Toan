package com.datn.maiphuctoandatn.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class OrderDetailKey {

    @Column(name = "product_id")
    Long ProductID;

    @Column(name = "order_id")
    Long OrderID;

    public OrderDetailKey(Long orderID, Long productID) {
        OrderID = orderID;
        ProductID = productID;
    }

    public OrderDetailKey() {
        
    }
}
