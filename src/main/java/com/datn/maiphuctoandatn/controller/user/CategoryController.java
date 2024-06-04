package com.datn.maiphuctoandatn.controller.user;

import com.datn.maiphuctoandatn.model.*;
import com.datn.maiphuctoandatn.service.cls.CategoryService;
import com.datn.maiphuctoandatn.service.cls.ProductService;
import com.datn.maiphuctoandatn.service.face.IStoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    IStoreService storeService;

    @GetMapping("/categories")
    public String GetAllCategories(Model model, HttpServletRequest request, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @ModelAttribute("keyword") String name) {
        Page<Categories> LsCategory = categoryService.getAllCategories(name, pageNo, 6);
        User user = (User) request.getSession().getAttribute("session_user");
        if (user == null) {
            user = new User();
        }
        List<Categories> categories = categoryService.getThreeCategories();

        Integer SizeProduct = (Integer) request.getSession().getAttribute("myCartNum");
        Store store = storeService.findStore();
        model.addAttribute("store", store);
        model.addAttribute("size_cart", SizeProduct);
        model.addAttribute("user", user);
        model.addAttribute("LsCategory", LsCategory);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("TotalPages", LsCategory.getTotalPages());
        model.addAttribute("curentPage", pageNo);
        model.addAttribute("keyword", name);
        model.addAttribute("categories", categories);

        return "user/Categories";
    }

    @GetMapping("/category/{id}")
    public String GetProductByCategory(Model model, HttpServletRequest request, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @ModelAttribute("keyword") String name, @ModelAttribute("sort") String option_sort, @ModelAttribute("search_by") String option_search ,@PathVariable("id") Long id) {
        Page<Product> LsProduct = productService.getAllProductByAuthor(id, option_sort, name, pageNo, 10);
        Categories category = categoryService.getCategoryById(id);
        Store store = storeService.findStore();
        List<Categories> categories = categoryService.getThreeCategories();

        User user = (User) request.getSession().getAttribute("session_user");
        if (user == null) {
            user = new User();
        }
        Integer SizeProduct = (Integer) request.getSession().getAttribute("myCartNum");
        String NotiMessage = (String) request.getSession().getAttribute("noti_message");
        request.getSession().setAttribute("noti_message", null);

        model.addAttribute("noti_message", NotiMessage);
        model.addAttribute("size_cart", SizeProduct);
        model.addAttribute("store", store);
        model.addAttribute("user", user);
        model.addAttribute("LsProduct", LsProduct);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("TotalPages", LsProduct.getTotalPages());
        model.addAttribute("curentPage", pageNo);
        model.addAttribute("option_sort", option_sort);
        model.addAttribute("keyword", name);
        model.addAttribute("categories", category);
        model.addAttribute("id", id);
        model.addAttribute("search_by", option_search);
        model.addAttribute("categories_menu", categories);
        model.addAttribute("url", "/category/"+id);

        return "user/CategoryDetail";
    }
}
