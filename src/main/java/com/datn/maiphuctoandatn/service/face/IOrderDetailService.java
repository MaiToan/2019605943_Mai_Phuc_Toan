package com.datn.maiphuctoandatn.service.face;

import com.datn.maiphuctoandatn.model.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    public List<OrderDetail> findOrderDetailByOrderId( Long orderId );

    public Integer GetSumQuantity();

    public Integer GetPercentSold();
}
