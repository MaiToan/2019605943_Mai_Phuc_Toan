package com.datn.maiphuctoandatn.controller.user;

import com.datn.maiphuctoandatn.model.*;
import com.datn.maiphuctoandatn.service.face.ICategoryService;
import com.datn.maiphuctoandatn.service.face.IFavoriteService;
import com.datn.maiphuctoandatn.service.face.IProductService;
import com.datn.maiphuctoandatn.service.face.IStoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FavoriteController {

    @Autowired
    IFavoriteService favoriteService;
    @Autowired
    IProductService productService;

    @Autowired
    IStoreService storeService;

    @Autowired
    ICategoryService categoryService;

    @GetMapping("/favorite")
    public String getFavorite(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("session_user");
        List<Categories> categories = categoryService.getThreeCategories();

        if (user == null) {
            request.getSession().setAttribute("noti_message", "Log in before using this function");
            return "redirect:/login";
        }
        List<Favorite> favoriteList = favoriteService.getAllFavoritesByUser(user.getId());
        Store store = storeService.findStore();

        List<Product> lsProducts = new ArrayList<>();
        for(Favorite favorite : favoriteList) {
            lsProducts.add(productService.getProductById(favorite.getProduct_id()));
        }
        model.addAttribute("store", store);
        model.addAttribute("favoriteList", lsProducts);
        model.addAttribute("categories", categories);

        return "Account";
    }

}
