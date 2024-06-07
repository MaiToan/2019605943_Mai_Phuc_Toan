package com.datn.maiphuctoandatn.controller.user;

import com.datn.maiphuctoandatn.config.FileUploadUtil;
import com.datn.maiphuctoandatn.model.*;
import com.datn.maiphuctoandatn.search.SearchOrder;
import com.datn.maiphuctoandatn.service.face.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class AccountController {
    @Autowired
    IUserService userService;

    @Autowired
    IFavoriteService favoriteService;

    @Autowired
    IProductService productService;

    @Autowired
    IStoreService storeService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ICategoryService categoryService;

    @Autowired
    IOrderService orderService;

    @GetMapping("/account")
    public String account(Model model, HttpServletResponse response, HttpServletRequest request, @ModelAttribute("SearchOrder") SearchOrder searchOrder) {
//        User
        User user = (User) request.getSession().getAttribute("session_user");
        if (user == null/* || user.getRole() ==1*/) {
            request.getSession().setAttribute("noti_message", "Log in before performing this function");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
            }
            return "redirect:/login";
        }
        List<Categories> categories = categoryService.getThreeCategories();

        String screenNumber = (String) request.getSession().getAttribute("check_screen");
        request.getSession().setAttribute("screen_number", null);
        if (screenNumber == null) screenNumber = "1";
//        Favorite
        List<Favorite> favoriteList = favoriteService.getAllFavoritesByUser(user.getId());

        List<Product> lsProducts = new ArrayList<>();
        for (Favorite favorite : favoriteList) {
            lsProducts.add(productService.getProductById(favorite.getProduct_id()));
        }
//        store
        Store store = storeService.findStore();
//        Order
        List<Order> orders = new ArrayList<>();
        if ((Objects.equals(searchOrder.getContent(), null) && Objects.equals(searchOrder.getStatus(), null) ||
                Objects.equals(searchOrder.getContent(), "") && Objects.equals(searchOrder.getStatus(), ""))) {
            orders = orderService.findOrderByUser(user);
        } else {
            String getContent = searchOrder.getContent();
            orders = orderService.findOrderByUserSearch(user, searchOrder);
            searchOrder.setContent(getContent);
        }
        if (orders.size() > 20) {
            orders = orders.subList(0, 20);
        }
        if (!Objects.equals(searchOrder.getContent(), null) || !Objects.equals(searchOrder.getStatus(), null))
            screenNumber = "2";
        for (Order order : orders) {
            String formatDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(order.getOrderDate());
            order.setOrderDateFormat(formatDate);
        }
        model.addAttribute("searchOrder", searchOrder);

        String message = (String) request.getSession().getAttribute("noti_message");
        request.getSession().setAttribute("noti_message", null);
        model.addAttribute("store", store);
        model.addAttribute("favoriteList", lsProducts);
        model.addAttribute("user", user);
        model.addAttribute("noti_message", message);
        model.addAttribute("orders", orders);
        model.addAttribute("screenNumber", screenNumber);
        model.addAttribute("categories", categories);

        return "user/Account";
    }

    @PostMapping("/account-user")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam("image") MultipartFile multipartFile, HttpServletRequest request
    ) throws IOException {
        String CheckFile = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String fileName = "";
        if (!CheckFile.isEmpty()) {
            fileName = String.valueOf(new Date().getTime()) + ".jpg";
            user.setAvatar(fileName);
        }
        User userUpdate = userService.updateUser(user);
        if (!CheckFile.isEmpty()) {
            String uploadDir = "src/main/upload/static/images/gallery";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        }
        request.getSession().setAttribute("session_user", userUpdate);
        request.getSession().setAttribute("noti_message", "Update information successful");
        request.getSession().setAttribute("check_screen", "1");
        return "redirect:/account";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute("password-old") String password, @ModelAttribute("password-new") String passNew,
                                 @ModelAttribute("repeat-pass") String reatPass, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("session_user");
        Boolean checkPass = userService.checkPassword(password, user.getPassword());
        if (checkPass == false) {
            request.getSession().setAttribute("noti_message", "Enter wrong password !");
            return "redirect:/account";
        }
        if (!passNew.equals(reatPass)) {
            request.getSession().setAttribute("noti_message", "The re-entered password does not match");
            return "redirect:/account";
        }
        User checkSuc = userService.changePass(user, passwordEncoder.encode(passNew));
        if (checkSuc == null) {
            request.getSession().setAttribute("noti_message", "Password change failed");
            return "redirect:/account";
        }
        request.getSession().setAttribute("check_screen", "4");
        request.getSession().setAttribute("noti_message", "changed password successfully");
        return "redirect:/account";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("noti_message", "Log out successfully");
        request.getSession().setAttribute("session_user", null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login";
    }
}
