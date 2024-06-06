package com.datn.maiphuctoandatn.controller.admin;


import com.datn.maiphuctoandatn.config.FileUploadUtil;
import com.datn.maiphuctoandatn.model.Author;
import com.datn.maiphuctoandatn.model.Categories;
import com.datn.maiphuctoandatn.model.Product;
import com.datn.maiphuctoandatn.service.face.ICategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("admin")
public class ManageCategoryController {

    @Autowired
    ICategoryService categoryService;

    @GetMapping("/ListCategory")
    public String ListCategory(Model model, HttpServletRequest request, @ModelAttribute("searchCategory") String searchCategory) {
        List<Categories> ListCategory = new ArrayList<>() ;
        if ((Objects.equals(searchCategory, null) ||
                Objects.equals(searchCategory, ""))) {
            ListCategory = categoryService.getAll();
        } else {
            ListCategory = categoryService.findCategoryBySearch(searchCategory);
        }
        for (Categories categories : ListCategory) {
            String formatDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(categories.getCreated_at());
            categories.setCreatedAtFormat(formatDate);
        }
        model.addAttribute("searchCategory", searchCategory);
        model.addAttribute("categories", ListCategory);
        return "admin/ListCategory";
    }

    @GetMapping("/detail-category/{id}")
    public String detailCategory(Model model, HttpServletRequest request, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                               @ModelAttribute("keyword") String name, @ModelAttribute("sort") String option_sort, @PathVariable("id") Long id) {
        Categories category = categoryService.getCategoryById(id);
        Page<Product> LsProduct = categoryService.getAllProductByCategory(id, option_sort, name, pageNo, 15);

        model.addAttribute("curentPage", pageNo);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("TotalPages", LsProduct.getTotalPages());
        model.addAttribute("option_sort", option_sort);
        model.addAttribute("keyword", name);
        model.addAttribute("category", category);
        model.addAttribute("LsProduct", LsProduct);

        return "admin/CategoryDetail";
    }

    @GetMapping("/edit-category/{id}")
    public String editCategory(Model model, @PathVariable("id") Long id) {
        Categories category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "/admin/EditCategory";
    }

    @PostMapping("/edit-category")
    public String EditCategory(@ModelAttribute("category") Categories category, @RequestParam("image_category") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        String CheckFile = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String fileName = "";
        if (!CheckFile.isEmpty()) {
            fileName = String.valueOf(new Date().getTime()) + ".jpg";
            category.setAvatar(fileName);
        }
        categoryService.updateCategory(category);
        if (!CheckFile.isEmpty()) {
            String uploadDir = "src/main/upload/static/images/gallery";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        }
        return "redirect:/admin/detail-category/" + category.getId();
    }

    @PostMapping("/delete-category")
    public String DeleteCategory(@ModelAttribute("id") Long id) {
        Categories category = categoryService.getCategoryById(id);
        category.setLOEVM("1");
        categoryService.deleteCategory(category);
        return "redirect:/admin/ListCategory";
    }

    @GetMapping("/NewCategory")
    public String NewCategory(Model model) {
        model.addAttribute("category", new Categories());
        return "admin/AddCategory";
    }

    @PostMapping("/add-category")
    public String AddAuthor(@ModelAttribute("category") Categories categories, @RequestParam("image_category") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        String CheckFile = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String fileName = "";
        if (!CheckFile.isEmpty()) {
            fileName = String.valueOf(new Date().getTime()) + ".jpg";
            categories.setAvatar(fileName);
        }
        categories.setLOEVM("0");
        categoryService.saveCategory(categories);
        if (!CheckFile.isEmpty()) {
            String uploadDir = "src/main/upload/static/images/gallery";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        }
        return "redirect:/admin/ListCategory";
    }
}
