package com.datn.maiphuctoandatn.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductAuthorKey {

    @Column(name = "product_id")
    Long ProductID;

    @Column(name = "author_id")
    Long AuthorID;

    public ProductAuthorKey(Long productID, Long authorID) {
        ProductID = productID;
        AuthorID = authorID;
    }
    public ProductAuthorKey() {
    }
}
