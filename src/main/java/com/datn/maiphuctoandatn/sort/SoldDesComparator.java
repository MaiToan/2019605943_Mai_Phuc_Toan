package com.datn.maiphuctoandatn.sort;

import com.datn.maiphuctoandatn.model.Product;

import java.util.Comparator;

public class SoldDesComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return Integer.compare(p2.ProductSold, p1.ProductSold);
    }
}