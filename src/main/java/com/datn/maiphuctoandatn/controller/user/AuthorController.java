package com.datn.maiphuctoandatn.controller.user;

import com.datn.maiphuctoandatn.model.*;
import com.datn.maiphuctoandatn.service.face.IAuthorService;
import com.datn.maiphuctoandatn.service.face.ICategoryService;
import com.datn.maiphuctoandatn.service.face.IProductService;
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
public class AuthorController {
    @Autowired
    private IAuthorService authorService;

    @Autowired
    private IProductService productService;

    @Autowired
    IStoreService storeService;

    @Autowired
    ICategoryService categoryService;

    @GetMapping("/authors")
    public String GetAllAuthor(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("session_user");
        if (user == null) {
            user = new User();
        }
        List<Categories> categories = categoryService.getThreeCategories();

        Store store = storeService.findStore();
        List<Author> authors = authorService.getAllAuthors();
        Integer SizeProduct = (Integer) request.getSession().getAttribute("myCartNum");
        model.addAttribute("store", store);
        model.addAttribute("authors", authors);
        model.addAttribute("size_cart", SizeProduct);
        model.addAttribute("user", user);
        model.addAttribute("categories", categories);

        return "user/Author";
    }

    @GetMapping("/author/{id}")
    public String GetProductByAuthor(Model model, HttpServletRequest request, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                       @ModelAttribute("keyword") String name, @ModelAttribute("sort") String option_sort,  @PathVariable("id") Long id) {
        Page<Product> LsProduct = productService.getAllProductByAuthor(id, option_sort, name, pageNo, 15);
        Author author = authorService.getAuthorById(id);
        User user = (User) request.getSession().getAttribute("session_user");
        if (user == null) {
            user = new User();
        }
        Store store = storeService.findStore();
        Integer SizeProduct = (Integer) request.getSession().getAttribute("myCartNum");
        List<Categories> categories = categoryService.getThreeCategories();

        for (Product product : LsProduct) {
            String price = String.format("%.1f", product.getPrice()-product.getSale());
            product.setUnitPrice(price);
        }
        model.addAttribute("store", store);
        model.addAttribute("size_cart", SizeProduct);
        model.addAttribute("user", user);
        model.addAttribute("LsProduct", LsProduct);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("TotalPages", LsProduct.getTotalPages());
        model.addAttribute("curentPage", pageNo);
        model.addAttribute("option_sort", option_sort);
        model.addAttribute("keyword", name);
        model.addAttribute("author", author);
        model.addAttribute("id", id);
        model.addAttribute("categories", categories);
        model.addAttribute("url", "/author/"+id);

        return "user/AuthorProduct";
    }

}
