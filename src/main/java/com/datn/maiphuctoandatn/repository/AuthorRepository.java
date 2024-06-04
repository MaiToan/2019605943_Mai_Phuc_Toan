package com.datn.maiphuctoandatn.repository;

import com.datn.maiphuctoandatn.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query(value = "SELECT * FROM authors WHERE loevm = 0", nativeQuery = true)
    List<Author> findAllAuthor();

    @Query(value = "SELECT * FROM authors WHERE id=:id AND loevm = 0", nativeQuery = true)
    Author getAuthorById(Long id);

    @Query(value = "SELECT * FROM authors WHERE name like %:search% or email like %:search% or address like %:search% ", nativeQuery = true)
    List<Author> getAuthorBySearch(String search);
}
