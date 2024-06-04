package com.datn.maiphuctoandatn.service.cls;

import com.datn.maiphuctoandatn.model.*;
import com.datn.maiphuctoandatn.repository.OrderDetailRepository;
import com.datn.maiphuctoandatn.repository.OrderRepository;
import com.datn.maiphuctoandatn.search.SearchOrder;
import com.datn.maiphuctoandatn.service.face.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderService implements IOrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Override
    public void saveData(Order order, List<SessionCart> sessionCart) {
        Order getOrderSave = orderRepository.save(order);
        Long id = getOrderSave.getId();

        for (SessionCart item : sessionCart) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId(new OrderDetailKey(id, item.getProduct().getId()));
            orderDetail.setOrderOrderDetail(getOrderSave);
            orderDetail.setProductOrder(item.getProduct());
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setUnitPrice(item.getProduct().getPrice() - item.getProduct().getSale());
            orderDetailRepository.save(orderDetail);
        }
    }

    @Override
    public List<Order> findOrderByUser(User user) {
        return orderRepository.findOrderByUser(user.getId());
    }

    @Override
    public List<Order> findOrderByUserSearch(User user, SearchOrder searchOrder) {
        if (!Objects.equals(searchOrder.getContent(), "") && !searchOrder.getContent().contains(".") && Integer.parseInt(searchOrder.getContent()) > 1000000) {
            searchOrder.setContent(String.valueOf(Integer.parseInt(searchOrder.getContent()) - 1000000));
            return orderRepository.findOrderByUserSearchID(user.getId(), searchOrder.getContent(), searchOrder.getStatus());
        }

        return orderRepository.findOrderByUserSearch(user.getId(), searchOrder.getContent(), searchOrder.getStatus());
    }

    @Override
    public List<Order> findOrderBySearch(SearchOrder searchOrder) {
        if (!Objects.equals(searchOrder.getContent(), "") && !searchOrder.getContent().contains(".") && Integer.parseInt(searchOrder.getContent()) > 1000000) {
            searchOrder.setContent(String.valueOf(Integer.parseInt(searchOrder.getContent()) - 1000000));
            return orderRepository.findOrderBySearchID(searchOrder.getContent());
        }

        return orderRepository.findOrderBySearch(searchOrder.getContent());
    }

    @Override
    public Order findOrderByIDAndUser(User user, Long id) {
        return orderRepository.findOrderByUserAndId(user.getId(), id);
    }

    @Override
    public List<Order> GetAllOrder() {
        return orderRepository.getAllOrder();
    }

    @Override
    public Integer getPercentOrder() {
        Integer TotalOrderCurrent = orderRepository.getPercentOrderCurrent().size();
        Integer TotalOrderLast = orderRepository.getPercentLastOrder().size();
        if (TotalOrderLast == 0) TotalOrderLast = TotalOrderCurrent;
        return (TotalOrderCurrent - TotalOrderLast) * 100 / TotalOrderLast;
    }

    @Override
    public Integer getPercentRevenue() {
        Integer TotalOrderCurrent = orderRepository.getPercentOrderCurrent().size();
        Integer TotalOrderLast = orderRepository.getPercentLastOrder().size();

        Double LastRevenue = 0.0;
        Double CurrentRevenue = 0.0;

        for (Order order : orderRepository.getPercentOrderCurrent())
            CurrentRevenue += order.getDisCount();
        for (Order order : orderRepository.getPercentLastOrder())
            LastRevenue += order.getDisCount();

        if (LastRevenue == 0) LastRevenue = CurrentRevenue;
        return (int) ((CurrentRevenue - LastRevenue) * 100 / LastRevenue);
    }

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findById(id).get();
    }

    @Override
    public void update(Order order) {
        orderRepository.save(order);
    }


}
