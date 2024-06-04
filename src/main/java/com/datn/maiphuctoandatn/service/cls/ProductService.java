package com.datn.maiphuctoandatn.service.cls;

import com.datn.maiphuctoandatn.model.*;
import com.datn.maiphuctoandatn.repository.AuthorRepository;
import com.datn.maiphuctoandatn.repository.AuthorsProductRepository;
import com.datn.maiphuctoandatn.repository.ProductRepository;
import com.datn.maiphuctoandatn.service.face.IProductService;
import com.datn.maiphuctoandatn.sort.PriceComparator;
import com.datn.maiphuctoandatn.sort.SoldComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService implements IProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    AuthorsProductRepository authorsProductRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Override
    public Page<Product> getAllProduct(String sortBy, Integer pageNo, Integer pageSize) {
        List<Product> list = productRepository.findAllProduct();
        if (Objects.equals(sortBy, "sort_price"))
            list.sort(new PriceComparator());
        else if (Objects.equals(sortBy, "sort_sold")) {
            list.sort(new SoldComparator());
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Integer start = (int) pageable.getOffset();
        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset() + pageable.getPageSize());
        List<Product> subList = list.subList(start, end);
        return new PageImpl<Product>(subList, pageable, list.size());
    }

    @Override
    public Page<Product> getProductByName(String sortBy, String name, Integer pageNo, Integer pageSize) {
        List<Product> list = productRepository.findByNameProduct(name);
        if (Objects.equals(sortBy, "sort_price"))
            list.sort(new PriceComparator());
        else if (Objects.equals(sortBy, "sort_sold")) {
            list.sort(new SoldComparator());
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Integer start = (int) pageable.getOffset();
        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset() + pageable.getPageSize());
        List<Product> subList = list.subList(start, end);
        return new PageImpl<Product>(subList, pageable, list.size());
    }

    @Override
    public Page<Product> getProductBySearchAuthor(String sortBy, String name, Integer pageNo, Integer pageSize) {
        List<Product> list = productRepository.findByNameAuthor(name);
        if (Objects.equals(sortBy, "sort_price"))
            list.sort(new PriceComparator());
        else if (Objects.equals(sortBy, "sort_sold")) {
            list.sort(new SoldComparator());
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Integer start = (int) pageable.getOffset();
        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset() + pageable.getPageSize());
        List<Product> subList = list.subList(start, end);
        return new PageImpl<Product>(subList, pageable, list.size());
    }

    @Override
    public Page<Product> getAllProductByAuthor(Long id, String sortBy, String name, Integer pageNo, Integer pageSize) {
        List<Product> list;
        if (Objects.equals(name, ""))
            list = productRepository.findProductByIdAuthor(id);
        else
            list = productRepository.findProductByIdAndNameAuthor(id, name);
        if (Objects.equals(sortBy, "sort_price"))
            list.sort(new PriceComparator());
        else if (Objects.equals(sortBy, "sort_sold")) {
            list.sort(new SoldComparator());
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Integer start = (int) pageable.getOffset();
        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset() + pageable.getPageSize());
        List<Product> subList = list.subList(start, end);
        return new PageImpl<Product>(subList, pageable, list.size());
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.GetProductById(id);
    }

    @Override
    public List<Product> getTop10Product() {
        return productRepository.GetTop10Product();
    }

    @Override
    public List<Product> GetAllProduct() {
        return productRepository.GetAllProduct();
    }

    @Override
    public List<Product> findProductBySearch(String Search) {
        return productRepository.getAllProductBySearch(Search);
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void updateProduct(Product product, List<Author> authors) {
        Product getProductDB = productRepository.GetProductById(product.getId());
        getProductDB.setName(product.getName());
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            getProductDB.setImage(product.getImage());
        }
        getProductDB.setSale(product.getSale());
        getProductDB.setCategories(product.getCategories());
        getProductDB.setDescription(product.getDescription());
        getProductDB.setPrice(product.getPrice());
        getProductDB.setProductNumber(product.getProductNumber());
        if (!Objects.equals(authors, null)) {
            List<ProductAuthor> productAuthors = authorsProductRepository.GetAuthorProduct(product.getId());
            if(!Objects.equals(productAuthors, null)) {
                authorsProductRepository.deleteAll(productAuthors);
            }
            for (Author item : authors){
                ProductAuthor productModel = new ProductAuthor();
                productModel.setId(new ProductAuthorKey(getProductDB.getId(), item.getId()));
                productModel.setProductAuthor(getProductDB);
                Author author = authorRepository.getAuthorById(item.getId());
                productModel.setAuthorProduct(author);
                authorsProductRepository.save(productModel);
            }
        }
         productRepository.save(getProductDB);
    }

    @Override
    public void saveProduct(Product product, List<Author> authors) {
       Product getProductDB =  productRepository.save(product);
        if (!Objects.equals(authors, null)) {
            List<ProductAuthor> productAuthors = authorsProductRepository.GetAuthorProduct(product.getId());
            if(!Objects.equals(productAuthors, null)) {
                authorsProductRepository.deleteAll(productAuthors);
            }
            for (Author item : authors){
                ProductAuthor productModel = new ProductAuthor();
                productModel.setId(new ProductAuthorKey(getProductDB.getId(), item.getId()));
                productModel.setProductAuthor(getProductDB);
                Author author = authorRepository.getAuthorById(item.getId());
                productModel.setAuthorProduct(author);
                authorsProductRepository.save(productModel);
            }
        }
    }

}
