package com.datn.maiphuctoandatn.service.cls;

import com.datn.maiphuctoandatn.model.Author;
import com.datn.maiphuctoandatn.model.Categories;
import com.datn.maiphuctoandatn.model.Product;
import com.datn.maiphuctoandatn.repository.CategoryRepository;
import com.datn.maiphuctoandatn.repository.ProductRepository;
import com.datn.maiphuctoandatn.service.face.ICategoryService;
import com.datn.maiphuctoandatn.sort.PriceComparator;
import com.datn.maiphuctoandatn.sort.SoldComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public Page<Categories> getAllCategories(String name, Integer pageNo, Integer pageSize ) {

        List<Categories> list;
        if (Objects.equals(name, ""))
            list = categoryRepository.findAllCategory();
        else
            list = categoryRepository.findCategoriesByNameCategory(name);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Integer start = (int) pageable.getOffset();
        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset() + pageable.getPageSize());
        List<Categories> subList = list.subList(start, end);
        return new PageImpl<Categories>(subList, pageable, list.size());
    }

    @Override
    public Categories getCategoryById(Long id) {
        return categoryRepository.getCategoryByID(id);
    }

    @Override
    public List<Categories> getAll() {
        return categoryRepository.getAll();
    }

    @Override
    public List<Categories> getThreeCategories() {
        List<Categories> categories = categoryRepository.getAll();
        if (categories.size() > 4)
         categories = categories.subList(0, 4);
        return categories;
    }

    @Override
    public List<Categories> findCategoryBySearch(String search) {
        return categoryRepository.GetCategoryBySearch(search);
    }

    @Override
    public Page<Product> getAllProductByCategory(Long id, String sortBy, String name, Integer pageNo, Integer pageSize) {
        List<Product> list;
        if (Objects.equals(name, ""))
            list = productRepository.findProductByIdCategory(id);
        else
            list = productRepository.findProductByIdAndNameCategory(id, name);
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
    public void updateCategory(Categories category) {
        Categories getcategoryDB = categoryRepository.getCategoryByID(category.getId());
        getcategoryDB.setName(category.getName());
        getcategoryDB.setDescription(category.getDescription());
        if (category.getAvatar() != null && !category.getAvatar().isEmpty())
            getcategoryDB.setAvatar(category.getAvatar());
        categoryRepository.save(getcategoryDB);
    }

    @Override
    public void deleteCategory(Categories categories) {
        categoryRepository.save(categories);
    }

    @Override
    public void saveCategory(Categories categories) {
        categoryRepository.save(categories);
    }
}
