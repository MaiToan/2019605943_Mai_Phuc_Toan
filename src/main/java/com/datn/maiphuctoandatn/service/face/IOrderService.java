package com.datn.maiphuctoandatn.service.face;

import com.datn.maiphuctoandatn.model.Order;
import com.datn.maiphuctoandatn.model.SessionCart;
import com.datn.maiphuctoandatn.model.User;
import com.datn.maiphuctoandatn.search.SearchOrder;

import java.util.List;

public interface IOrderService {
    public void saveData(Order order, List<SessionCart> sessionCart);

    public List<Order> findOrderByUser(User user);

    public List<Order> findOrderByUserSearch(User user , SearchOrder searchOrder);

    public List<Order> findOrderBySearch(SearchOrder searchOrder);

    public Order findOrderByIDAndUser(User user , Long id);

    public List<Order> GetAllOrder();

    public Integer getPercentOrder();

    public Integer getPercentRevenue();

    public Order findOrderById(Long id);
    public void update(Order order);
}
