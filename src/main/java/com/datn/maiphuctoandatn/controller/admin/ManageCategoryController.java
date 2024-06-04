package com.datn.maiphuctoandatn.controller.admin;


import com.datn.maiphuctoandatn.model.Categories;
import com.datn.maiphuctoandatn.service.face.ICategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
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
        model.addAttribute("searchAuthor", searchCategory);
        model.addAttribute("categories", ListCategory);
        return "admin/ListCategory";
    }
}
