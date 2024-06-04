package com.datn.maiphuctoandatn.sort;

import com.datn.maiphuctoandatn.model.Product;
import com.datn.maiphuctoandatn.model.User;

import java.util.Comparator;

public class OrdersComparator implements Comparator<User> {
    @Override
    public int compare(User o1, User o2) {
        return Integer.compare(o1.getOrders_user().size(), o2.getOrders_user().size());
    }
}

