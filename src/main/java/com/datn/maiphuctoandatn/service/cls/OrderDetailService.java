package com.datn.maiphuctoandatn.service.cls;

import com.datn.maiphuctoandatn.model.OrderDetail;
import com.datn.maiphuctoandatn.repository.OrderDetailRepository;
import com.datn.maiphuctoandatn.service.face.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService implements IOrderDetailService {
    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetail> findOrderDetailByOrderId( Long OrderID) {
        return orderDetailRepository.findAllOrderByIDOrder(OrderID);
    }

    @Override
    public Integer GetSumQuantity() {
        return orderDetailRepository.GetAllQuantity();
    }

    @Override
    public Integer GetPercentSold() {
        Integer percentSoldCurrent = orderDetailRepository.GetQuantityCurrent();
        Integer percentSoldLast = orderDetailRepository.GetQuantityLast();
        if (percentSoldLast == 0) percentSoldLast = percentSoldCurrent;
        return (percentSoldCurrent-percentSoldLast)*100/percentSoldLast;
    }


}
