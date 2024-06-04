package com.datn.maiphuctoandatn.repository;

import com.datn.maiphuctoandatn.model.Comment;
import com.datn.maiphuctoandatn.model.ProductAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorsProductRepository extends JpaRepository<ProductAuthor, Long> {
    @Query(value = "SELECT * FROM product_author where product_id =:product_id", nativeQuery = true)
    public List<ProductAuthor> GetAuthorProduct(Long product_id);
}
