package com.datn.maiphuctoandatn.controller.admin;


import com.datn.maiphuctoandatn.model.Order;
import com.datn.maiphuctoandatn.model.OrderDetail;
import com.datn.maiphuctoandatn.model.User;
import com.datn.maiphuctoandatn.service.face.IOrderService;
import com.datn.maiphuctoandatn.service.face.IUserService;
import com.datn.maiphuctoandatn.sort.OrderComparatorDes;
import com.datn.maiphuctoandatn.sort.OrdersComparator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("admin")
public class ManageUsersController {

    @Autowired
    IUserService userService;

    @Autowired
    IOrderService orderService;

    @GetMapping("/ListUser")
    public String ListUser(Model model, HttpServletRequest request, @ModelAttribute("searchUser") String searchUser, @ModelAttribute("Sort-Order") String sortOrder) {
        List<User> ListUser = new ArrayList<>() ;
        if ((Objects.equals(searchUser, null) ||
                Objects.equals(searchUser, ""))) {
            ListUser = userService.getAllUsers();
        } else {
            ListUser = userService.findUserBySearch(searchUser);
        }
        if (sortOrder.equals("asc")) {
            ListUser.sort(new OrdersComparator());
        } else if (sortOrder.equals("desc")) {
            ListUser.sort(new OrderComparatorDes());
        }
        model.addAttribute("searchUser", searchUser);
        model.addAttribute("users", ListUser);
        return "admin/ListUser";
    }


    @GetMapping("/UserDetail/{id}")
    public String ListOrders(Model model, HttpServletRequest request, @PathVariable("id") Long id) {
        User user = userService.findUserByID(id);
        Date dateTime = new Date(user.getCreated_at().getTime());
        List<Order> orders = orderService.findOrderByUser(user);

        model.addAttribute("User", user);
        model.addAttribute("dateTime", dateTime);
        model.addAttribute("orders", orders);
        return "admin/DetailUser";
    }

    @PostMapping("/ChangeActive")
    public String ChangeActive(Model model, HttpServletRequest request, @ModelAttribute("User") User user, @ModelAttribute("ID") Long id) {
        User getUserDB = userService.findUserByID(id);
        getUserDB.setActive(user.getActive());
        userService.updateUser(getUserDB);
        return "redirect:/admin/UserDetail/"+id;
    }
}
