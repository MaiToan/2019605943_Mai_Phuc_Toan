package com.datn.maiphuctoandatn.service.face;

import com.datn.maiphuctoandatn.model.Author;
import com.datn.maiphuctoandatn.model.Favorite;
import com.datn.maiphuctoandatn.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface IProductService {
    public Page<Product> getAllProduct(String sortBy, Integer pageNo, Integer pageSize); ;

    public Page<Product> getProductByName(String sortBy, String name, Integer pageNo, Integer pageSize);

    public Page<Product> getProductBySearchAuthor(String sortBy, String name, Integer pageNo, Integer pageSize);

    public Page<Product> getAllProductByAuthor(Long id, String sortBy, String name, Integer pageNo, Integer pageSize);

    public Product getProductById(Long id);

    public List<Product> getTop10Product();

    public List<Product> GetAllProduct();

    public List<Product> findProductBySearch(String Search);

    public void saveProduct(Product product);

    public void deleteProduct(Product product);

    public void updateProduct(Product product, List<Author> author);

    public void saveProduct(Product product, List<Author> author);



}
