package com.datn.maiphuctoandatn.controller.user;

import com.datn.maiphuctoandatn.model.*;
import com.datn.maiphuctoandatn.service.face.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
public class ProductController {
    @Autowired
    IProductService productService;
    @Autowired
    ICommetService commetService;
    @Autowired
    ICategoryService categoryService;
    @Autowired
    IAuthorService authorService;
    @Autowired
    IFavoriteService favoriteService;

    @Autowired
    IStoreService storeService;

    @GetMapping("/products")
    public String GetAllProduct(Model model, HttpServletRequest request, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @ModelAttribute("keyword") String name, @ModelAttribute("searchBy") String option_search, @ModelAttribute("sort") String option_sort) {
        Page<Product> LsProduct = productService.getAllProduct(option_sort, pageNo, 16);
        if (!Objects.equals(name, "")) {
            if (Objects.equals(option_search, "search_product"))
                LsProduct = productService.getProductByName(option_sort, name, pageNo, 16);
            else if (Objects.equals(option_search, "search_author")) {
                LsProduct = productService.getProductBySearchAuthor(option_sort, name, pageNo, 16);
                model.addAttribute("keyword", name);
            }

        }
        String NotiMessage = (String) request.getSession().getAttribute("noti_message");
        request.getSession().setAttribute("noti_message", null);
        User user = (User) request.getSession().getAttribute("session_user");
        if (user == null) {
            user = new User();
        }
        Integer SizeProduct = (Integer) request.getSession().getAttribute("myCartNum");
        Store store = storeService.findStore();
        List<Categories> categoriesThree = categoryService.getThreeCategories();

        List<Categories> Categories = categoryService.getAll();
        List<Author> authors = authorService.getAllAuthors();
        model.addAttribute("store", store);
        model.addAttribute("user", user);
        model.addAttribute("LsProduct", LsProduct);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("TotalPages", LsProduct.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("option_sort", option_sort);
        model.addAttribute("search_by", option_search);
        model.addAttribute("url", "/products");
        model.addAttribute("noti_message", NotiMessage);
        model.addAttribute("size_cart", SizeProduct);
        model.addAttribute("Categories", Categories);
        model.addAttribute("Authors", authors);
        model.addAttribute("categories_menu", categoriesThree);

        return "user/Product";
    }

    @GetMapping("/DetailProduct/{id}")
    public String GetProduct(Model model, HttpServletRequest request, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @PathVariable("id") Long id) {
        User user = (User) request.getSession().getAttribute("session_user");
        if (user == null) {
            user = new User();
        }
        String noti_message = (String) request.getSession().getAttribute("noti_message");
        Integer SizeProduct = (Integer) request.getSession().getAttribute("myCartNum");

        request.getSession().setAttribute("noti_message", null);
        Product product = productService.getProductById(id);
        List<ProductAuthor> authors = product.getProductAuthors();
        Page<Comment> lsComments = commetService.getCommentByProduct(id, pageNo, 3);
        List<Comment> comments = commetService.GetCommentList(id);
        List<Product> lsTop10 = productService.getTop10Product();
        Store store = storeService.findStore();
        List<Categories> categories = categoryService.getThreeCategories();



        Favorite favorite = new Favorite() ;
        if (user != null)
            favorite = favoriteService.getFavoriteByUserProduct(id, user.getId());
        int subNum = lsTop10.size() / 4;
        List<Product> sub1List = lsTop10.subList(0, subNum);
        List<Product> sub2List = lsTop10.subList(subNum, subNum * 2);
        List<Product> sub3List = lsTop10.subList(subNum * 2, subNum * 3);
        List<Product> sub4List = lsTop10.subList(subNum * 3, subNum * 4);
        Comment comment = new Comment();
        comment.RateNumber = 5;
        int One = 0;
        int Two = 0;
        int Three = 0;
        int Four = 0;
        int Five = 0;
        Double AvgRate = 0.0;
        for (Comment item : comments) {
            AvgRate += item.getRateNumber();
            switch (item.getRateNumber()) {
                case 1:
                    One += 1;
                    break;
                case 2:
                    Two += 1;
                    break;
                case 3:
                    Three += 1;
                    break;
                case 4:
                    Four += 1;
                    break;
                case 5:
                    Five += 1;
                    break;
            }
        }
        if (!comments.isEmpty())
            AvgRate /= comments.size();

        model.addAttribute("url", "/DetailProduct/" + id);
        model.addAttribute("store", store);
        model.addAttribute("size_cart", SizeProduct);
        model.addAttribute("noti_message", noti_message);
        model.addAttribute("newcomment", comment);
        model.addAttribute("comments", comments);
        model.addAttribute("user", user);
        model.addAttribute("product", product);
        model.addAttribute("authors", authors);
        model.addAttribute("id_product", id);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("lsComment", lsComments);
        model.addAttribute("TotalPages", lsComments.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("avgRate", AvgRate);
        model.addAttribute("One", One);
        model.addAttribute("Two", Two);
        model.addAttribute("Three", Three);
        model.addAttribute("Four", Four);
        model.addAttribute("Five", Five);
        model.addAttribute("lsTopTen", lsTop10);
        model.addAttribute("sub1List", sub1List);
        model.addAttribute("sub2List", sub2List);
        model.addAttribute("sub3List", sub3List);
        model.addAttribute("sub4List", sub4List);
        model.addAttribute("favorite", favorite);
        model.addAttribute("categories", categories);

        return "user/DetailProduct";
    }

    @PostMapping("/DetailProduct/{id}")
    public String DetailProduct(Model model, @ModelAttribute("comment") Comment comment, HttpServletRequest request, @PathVariable("id") Long id) {
        User user = (User) request.getSession().getAttribute("session_user");
        if (user == null) {
            return "redirect:/login";
        }
        Comment CheckComment = commetService.getCommentByProductAndUser(user.getId(), id);
        if (CheckComment != null) return "redirect:/DetailProduct/" + id;
        Product product = productService.getProductById(id);
        comment.setProduct(product);
        comment.Loevm = "0";
        comment.setTopComment(0);
        comment.setUser(user);
        commetService.saveData(comment);
        return "redirect:/DetailProduct/" + id;
    }


    @PostMapping("/btn-favorite")
    public String favorite(Model model, @ModelAttribute("id") Long id, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("session_user");
        if (user == null) {
            request.getSession().setAttribute("noti_message", "Log in before performing this function");
            return "redirect:/DetailProduct/" + id;
        }
        Favorite favorite = favoriteService.getFavoriteByUserProduct(id, user.getId());
        if (favorite != null) {
            favoriteService.remove(favorite);
            request.getSession().setAttribute("noti_message", "Removed product from favorites");
        } else {
            favoriteService.save(new Favorite( id, user.getId()));
            request.getSession().setAttribute("noti_message", "Added product to favorites");
        }
        return "redirect:/DetailProduct/" + id;
    }
}

