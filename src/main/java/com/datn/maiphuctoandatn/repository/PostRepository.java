package com.datn.maiphuctoandatn.repository;

import com.datn.maiphuctoandatn.model.Author;
import com.datn.maiphuctoandatn.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT * FROM posts WHERE loevm = 0", nativeQuery = true)
    List<Post> getAll();
}
