package com.datn.maiphuctoandatn.controller.user;

import com.datn.maiphuctoandatn.model.*;
import com.datn.maiphuctoandatn.service.face.ICategoryService;
import com.datn.maiphuctoandatn.service.face.IOrderDetailService;
import com.datn.maiphuctoandatn.service.face.IOrderService;
import com.datn.maiphuctoandatn.service.face.IStoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    IOrderService orderService;

    @Autowired
    IStoreService storeService;

    @Autowired
    IOrderDetailService orderDetailService;

    @Autowired
    ICategoryService categoryService;

    @GetMapping("/OrderDetail/{id}")
    public String DetailOrder(Model model, @PathVariable Long id, HttpServletRequest request) {
        Integer SizeProduct = (Integer) request.getSession().getAttribute("myCartNum");

        User user = (User) request.getSession().getAttribute("session_user");
        if (user == null) {
            request.getSession().setAttribute("noti_message", "Log in before performing this function");
            return "redirect:/login";
        }
        Order order = orderService.findOrderByIDAndUser(user, id);

        String formatDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(order.getOrderDate());
        order.setOrderDateFormat(formatDate);
        List<Categories> categories = categoryService.getThreeCategories();

        List<OrderDetail> orderDetails = orderDetailService.findOrderDetailByOrderId(id);
//        Date dateTime = new Date(order.getOrderDate().getTime());
        Store store = storeService.findStore();
        model.addAttribute("store", store);
        model.addAttribute("size_cart", SizeProduct);
        model.addAttribute("user", user);
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
//        model.addAttribute("dateTime", dateTime);
        model.addAttribute("categories", categories);

        return "user/OrderDetail";
    }
}
