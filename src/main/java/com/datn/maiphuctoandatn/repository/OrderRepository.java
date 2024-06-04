package com.datn.maiphuctoandatn.repository;

import com.datn.maiphuctoandatn.model.Order;
import com.datn.maiphuctoandatn.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT * FROM orders where user_id =:userId order by order_date desc ", nativeQuery = true)
    List<Order> findOrderByUser(Long userId);

    @Query(value = "SELECT * FROM orders where user_id =:userId and ( id like %:content% or discount like %:content%) and status like %:status% order by created_at desc ", nativeQuery = true)
    List<Order> findOrderByUserSearch(Long userId, String content, String status);

    @Query(value = "SELECT * FROM orders where user_id =:userId and id like %:content% and status like %:status% order by created_at desc ", nativeQuery = true)
    List<Order> findOrderByUserSearchID(Long userId, String content, String status);

    @Query(value = "SELECT * FROM orders where ( id like %:content% or discount like %:content%) order by created_at desc", nativeQuery = true)
    List<Order> findOrderBySearch( String content);

    @Query(value = "SELECT * FROM orders where id like %:content% order by created_at desc ", nativeQuery = true)
    List<Order> findOrderBySearchID( String content);

    @Query(value = "SELECT * FROM orders where user_id =:userId and id=:id", nativeQuery = true)
    Order findOrderByUserAndId(Long userId, Long id);

    @Query(value = "SELECT * FROM orders where status != 0 order by status", nativeQuery = true)
    List<Order> getAllOrder();

    @Query(value = "SELECT * FROM orders WHERE MONTH(created_at) = MONTH(CURRENT_TIMESTAMP) AND YEAR(created_at) = YEAR(CURRENT_TIMESTAMP) AND status != 0", nativeQuery = true)
    List<Order> getPercentOrderCurrent();

    @Query(value = "SELECT * FROM orders WHERE MONTH(created_at) = MONTH(CURRENT_TIMESTAMP)-1 AND YEAR(created_at) = YEAR(CURRENT_TIMESTAMP) AND status != 0", nativeQuery = true)
    List<Order> getPercentLastOrder();
}
