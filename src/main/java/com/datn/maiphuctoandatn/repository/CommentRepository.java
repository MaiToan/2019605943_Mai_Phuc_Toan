package com.datn.maiphuctoandatn.repository;

import com.datn.maiphuctoandatn.model.Categories;
import com.datn.maiphuctoandatn.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT * FROM comment where product_id =:product_id and user_id =:user_id and loevm = 0", nativeQuery = true)
    public Comment ckeComment(Long product_id, Long user_id);

    @Query(value = "SELECT * FROM comment where product_id =:product_id", nativeQuery = true)
    public List<Comment> GetCommentByProduct(Long product_id);

    @Query(value = "SELECT * FROM comment where top_comment = 1 and loevm = 0 ", nativeQuery = true)
    public List<Comment> getTopComment();
}
