package com.datn.maiphuctoandatn.controller.admin;

import com.datn.maiphuctoandatn.model.Order;
import com.datn.maiphuctoandatn.search.SearchOrder;
import com.datn.maiphuctoandatn.service.face.IOrderDetailService;
import com.datn.maiphuctoandatn.service.face.IOrderService;
import com.datn.maiphuctoandatn.service.face.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
//@RequestMapping("admin")
public class HomeAdminController {

    @Autowired
    IUserService userService;

    @Autowired
    IOrderService orderService;

    @Autowired
    IOrderDetailService orderDetailService;

    @GetMapping("/admin/Home")
    public String HomeAdmin(Model model , HttpServletRequest request, @ModelAttribute("SearchOrder") SearchOrder searchOrder) {
//        user
        Integer NumberUser = userService.getAllUsers().size();
        Integer CompareLastMonth = userService.getPercentUser();
//        order
        List<Order> orders = orderService.GetAllOrder();
        Integer TotalOrders = orders.size();
        Integer PercentOrder = orderService.getPercentOrder();
//        RENEVER
        Double Revenue = 0.0;
        for (Order order : orders) {
            Revenue += order.getDisCount();
        }
        String total_revenue = String.format("%.1f", Revenue);
        Integer PercentRevenue = orderService.getPercentRevenue();
//sold
        Integer TotalSold = orderDetailService.GetSumQuantity();
        Integer ToTalPercent = orderDetailService.GetPercentSold();
//search
        List<Order> ListOrder = new ArrayList<>() ;
        if ((Objects.equals(searchOrder.getContent(), null) && Objects.equals(searchOrder.getStatus(), null) ||
                Objects.equals(searchOrder.getContent(), "") && Objects.equals(searchOrder.getStatus(), ""))) {
            ListOrder = orderService.GetAllOrder();
        } else {
            String getContent = searchOrder.getContent();
            ListOrder = orderService.findOrderBySearch( searchOrder);
            searchOrder.setContent(getContent);
        }
        if (ListOrder.size() > 20) {
            ListOrder = orders.subList(0, 20);
        }
        for (Order order : orders) {
            String formatDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(order.getOrderDate());
            order.setOrderDateFormat(formatDate);
        }
        model.addAttribute("searchOrder", searchOrder);
        model.addAttribute("NumberUser", NumberUser);
        model.addAttribute("CompareLastMonth", CompareLastMonth);

        model.addAttribute("PercentOrder", PercentOrder);
        model.addAttribute("TotalOrders", TotalOrders);

        model.addAttribute("Revenue", total_revenue);
        model.addAttribute("PercentRevenue", PercentRevenue);

        model.addAttribute("TotalSold", TotalSold);
        model.addAttribute("ToTalPercent", ToTalPercent);

        model.addAttribute("orders", ListOrder);
        return "admin/HomeAdmin";
    }
}
