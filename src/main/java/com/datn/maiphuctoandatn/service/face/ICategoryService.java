package com.datn.maiphuctoandatn.service.face;

import com.datn.maiphuctoandatn.model.Author;
import com.datn.maiphuctoandatn.model.Categories;
import com.datn.maiphuctoandatn.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICategoryService {
    public Page<Categories> getAllCategories(String name, Integer pageNo, Integer pageSize );

    public Categories getCategoryById(Long id);

    public List<Categories> getAll();

    public List<Categories> getThreeCategories();

    public List<Categories> findCategoryBySearch(String search);

    public Page<Product> getAllProductByCategory(Long id, String sortBy, String name, Integer pageNo, Integer pageSize);

    public void updateCategory(Categories category);

    public void deleteCategory(Categories categories);

    public void saveCategory(Categories categories);

}
