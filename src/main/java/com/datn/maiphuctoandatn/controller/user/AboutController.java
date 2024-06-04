package com.datn.maiphuctoandatn.controller.user;

import com.datn.maiphuctoandatn.model.Categories;
import com.datn.maiphuctoandatn.model.Post;
import com.datn.maiphuctoandatn.model.Store;
import com.datn.maiphuctoandatn.model.User;
import com.datn.maiphuctoandatn.service.face.ICategoryService;
import com.datn.maiphuctoandatn.service.face.IPostService;
import com.datn.maiphuctoandatn.service.face.IStoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AboutController {

    @Autowired
    IStoreService storeService;

    @Autowired
    ICategoryService categoryService;


    @Autowired
    IPostService postService;
    @GetMapping("/about")
    public String Home(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("session_user");
        if (user == null) {
            user = new User();
        }
        Store store = storeService.findStore();
        Integer SizeProduct = (Integer) request.getSession().getAttribute("myCartNum");
        Post post = postService.getPost();
        List<Categories> categories = categoryService.getThreeCategories();


        model.addAttribute("store", store);
        model.addAttribute("size_cart", SizeProduct);
        model.addAttribute("user", user);
        model.addAttribute("post", post);
        model.addAttribute("categories", categories);

        return "user/About";
    }
}
