package com.datn.maiphuctoandatn.repository;

import com.datn.maiphuctoandatn.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query(value = "SELECT * FROM favorite where user_id=:userID and product_id=:productID", nativeQuery = true)
    Favorite getFavoriteByUserProduct(Long userID, Long productID);

    @Query(value = "SELECT * FROM favorite where user_id=:userID", nativeQuery = true)
    List<Favorite> getFavoriteByUser(Long userID);
}
