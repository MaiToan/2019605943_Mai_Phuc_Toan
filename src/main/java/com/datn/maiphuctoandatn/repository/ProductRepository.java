package com.datn.maiphuctoandatn.repository;

import com.datn.maiphuctoandatn.model.Favorite;
import com.datn.maiphuctoandatn.model.Product;
import com.datn.maiphuctoandatn.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM products where name like %:name% and products.loevm = '0'", nativeQuery = true)
    List<Product> findByNameProduct(String name);

    @Query(value = "SELECT products.* FROM products inner join product_author on products.id = product_author.product_id inner join authors on product_author.author_id = authors.id where authors.name like %:name% and products.loevm = '0'", nativeQuery = true)
    List<Product> findByNameAuthor(String name);

    @Query(value = "SELECT * FROM products where products.loevm = '0' order by created_at desc", nativeQuery = true)
    List<Product> findAllProduct();

    @Query(value = "SELECT products.* FROM products inner join product_author on products.id = product_author.product_id where product_author.author_id =:id and products.loevm = '0'", nativeQuery = true)
    List<Product> findProductByIdAuthor(Long id);

    @Query(value = "SELECT products.* FROM products inner join product_author on products.id = product_author.product_id where product_author.author_id =:id and products.name like %:name% and products.loevm = '0'", nativeQuery = true)
    List<Product> findProductByIdAndNameAuthor(Long id, String name);

    @Query(value = "SELECT * FROM products where products.loevm = '0' and id=:id", nativeQuery = true)
    Product GetProductById(Long id);

    @Query(value = "SELECT top 10 * FROM products where products.loevm = '0' order by product_sold", nativeQuery = true)
    List<Product> GetTop10Product();

    @Query(value = "SELECT * FROM products where products.loevm = '0' order by created_at desc", nativeQuery = true)
    List<Product> GetAllProduct();

    @Query(value = "SELECT products.* FROM products inner join categories on products.category_id = categories.id where products.loevm = '0' AND ( products.name like %:Search% OR price like %:Search% or categories.name like %:Search%)", nativeQuery = true)
    List<Product> getAllProductBySearch(String Search);

    @Query(value = "SELECT * FROM products where category_id =:id and loevm = 0", nativeQuery = true)
    List<Product> findProductByIdCategory(Long id);

    @Query(value = "SELECT * FROM products where name like %:name% and category_id =:id and loevm = 0", nativeQuery = true)
    List<Product> findProductByIdAndNameCategory(Long id, String name);
}
