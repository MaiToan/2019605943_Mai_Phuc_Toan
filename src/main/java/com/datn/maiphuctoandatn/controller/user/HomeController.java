package com.datn.maiphuctoandatn.controller.user;

import com.datn.maiphuctoandatn.model.*;
import com.datn.maiphuctoandatn.service.face.IAuthorService;
import com.datn.maiphuctoandatn.service.face.ICategoryService;
import com.datn.maiphuctoandatn.service.face.IProductService;
import com.datn.maiphuctoandatn.service.face.IStoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    IStoreService storeService;

    @Autowired
    ICategoryService categoryService;

    @Autowired
    IAuthorService authorService;

    @Autowired
    IProductService productService;

    @GetMapping("/Home")
    public String Home( Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("session_user");
        if (user == null ) {
            user = new User();
        }
        List<Author> authors = authorService.getAllAuthors();
        if (authors.size() > 5){
            authors = authors.subList(0, 5);
        }
        List<Categories> categories = categoryService.getThreeCategories();
        Store store = storeService.findStore();
        List<Product> lsProduct = productService.getTop10Product();
        if (lsProduct.size() > 4){
            lsProduct = lsProduct.subList(0, 4);
        }
        Integer SizeProduct = (Integer) request.getSession().getAttribute("myCartNum");
        String message = (String) request.getSession().getAttribute("noti_message" );
        request.getSession().setAttribute("noti_message", null );
        model.addAttribute("store", store);
        model.addAttribute("size_cart", SizeProduct);
        model.addAttribute("user", user);
        model.addAttribute("categories", categories);
        model.addAttribute("authors", authors);
        model.addAttribute("lsProduct", lsProduct);
        model.addAttribute("url", "/Home");
        model.addAttribute("noti_message", message);
        return "user/Home";
    }
}
