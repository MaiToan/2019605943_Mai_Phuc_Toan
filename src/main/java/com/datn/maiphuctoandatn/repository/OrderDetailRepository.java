package com.datn.maiphuctoandatn.repository;

import com.datn.maiphuctoandatn.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query(value = "SELECT * FROM order_detail where order_id =:orderId", nativeQuery = true)
    public List<OrderDetail> findAllOrderByIDOrder(Long orderId);

    @Query(value = "SELECT sum(quantity) FROM order_detail inner join orders on order_detail.order_id = orders.id where status != 0", nativeQuery = true)
    public Integer GetAllQuantity();

    @Query(value = "SELECT sum(quantity) FROM order_detail inner join orders on order_detail.order_id = orders.id where status != 0 and MONTH(created_at) = MONTH(CURRENT_TIMESTAMP) AND YEAR(created_at) = YEAR(CURRENT_TIMESTAMP)", nativeQuery = true)
    public Integer GetQuantityCurrent();

    @Query(value = "  SELECT sum(quantity) FROM order_detail inner join orders on order_detail.order_id = orders.id where status != 0 and MONTH(order_date) = MONTH(CURRENT_TIMESTAMP)-1 AND YEAR(order_date) = YEAR(CURRENT_TIMESTAMP)", nativeQuery = true)
    public Integer GetQuantityLast();
}
