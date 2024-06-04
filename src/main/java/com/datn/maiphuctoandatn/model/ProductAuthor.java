package com.datn.maiphuctoandatn.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProductAuthor {

    @EmbeddedId
    ProductAuthorKey id;

    @ManyToOne
    @MapsId("AuthorID")
    @JoinColumn(name = "author_id")
    Author AuthorProduct;

    @ManyToOne
    @MapsId("ProductID")
    @JoinColumn(name = "product_id")
    Product ProductAuthor;

}
