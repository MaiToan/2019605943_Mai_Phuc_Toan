package com.datn.maiphuctoandatn.sort;

import com.datn.maiphuctoandatn.model.Product;

import java.util.Comparator;

public class PriceComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return Double.compare(p1.Price, p2.Price);
    }
}
