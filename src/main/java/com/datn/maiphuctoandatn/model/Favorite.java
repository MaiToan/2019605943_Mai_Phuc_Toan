package com.datn.maiphuctoandatn.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "favorite")
public class Favorite {
    @Id
    private Long product_id;
    private Long user_id;

    public Favorite(Long product_id, Long user_id) {
        this.product_id = product_id;
        this.user_id = user_id;
    }
    public Favorite() {
    }
}
