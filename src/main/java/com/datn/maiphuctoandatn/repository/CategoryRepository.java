package com.datn.maiphuctoandatn.repository;

import com.datn.maiphuctoandatn.model.Author;
import com.datn.maiphuctoandatn.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Categories, Long> {

    @Query(value = "SELECT * FROM categories where loevm = 0", nativeQuery = true)
    List<Categories> findAllCategory();

    @Query(value = "SELECT * FROM categories where loevm = 0 and name like %:name%", nativeQuery = true)
    List<Categories> findCategoriesByNameCategory(String name);

    @Query(value = "SELECT * FROM categories where loevm = 0 and id=:id", nativeQuery = true)
    Categories getCategoryByID(Long id);

    @Query(value = "SELECT * FROM categories where loevm = 0", nativeQuery = true)
    List<Categories> getAll();

    @Query(value = "SELECT * FROM categories where loevm = 0 and ( id like %:search% or name like %:search%) ", nativeQuery = true)
    List<Categories> GetCategoryBySearch(String search);
}
