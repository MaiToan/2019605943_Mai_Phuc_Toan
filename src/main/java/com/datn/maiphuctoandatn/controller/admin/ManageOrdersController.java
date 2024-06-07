package com.datn.maiphuctoandatn.controller.admin;

import com.datn.maiphuctoandatn.model.Order;
import com.datn.maiphuctoandatn.model.OrderDetail;
import com.datn.maiphuctoandatn.model.User;
import com.datn.maiphuctoandatn.search.SearchOrder;
import com.datn.maiphuctoandatn.service.face.IOrderDetailService;
import com.datn.maiphuctoandatn.service.face.IOrderService;
import com.datn.maiphuctoandatn.service.face.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("admin/")
public class ManageOrdersController {

    @Autowired
    IUserService userService;

    @Autowired
    IOrderService orderService;

    @Autowired
    IOrderDetailService orderDetailService;

    @GetMapping("/ListOrders")
    public String ListOrders(Model model, HttpServletRequest request, @ModelAttribute("SearchOrder") SearchOrder searchOrder, @ModelAttribute("OrderFilter") Order orderFilter) {
        List<Order> ListOrder = new ArrayList<>();
        if ((Objects.equals(searchOrder.getContent(), null) ||
                Objects.equals(searchOrder.getContent(), ""))) {
            ListOrder = orderService.GetAllOrder();
        } else {
//            String getContent = searchOrder.getContent();
            ListOrder = orderService.findOrderBySearch(searchOrder);
//            searchOrder.setContent(getContent);
        }
        for (Order order : ListOrder) {
            String formatDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(order.getOrderDate());
            order.setOrderDateFormat(formatDate);
        }
        if (Objects.equals(orderFilter.status, null)) {
            orderFilter = new Order();
        } else {
            List<Order> ListOrderTemporary = new ArrayList<>();
            for (Order order : ListOrder) {
                if (order.status == orderFilter.status) {
                    ListOrderTemporary.add(order);
                }
            }
            ListOrder = null;
            ListOrder = ListOrderTemporary;
        }
        User user = (User) request.getSession().getAttribute("session_admin");
        model.addAttribute("user", user);
        model.addAttribute("searchOrder", searchOrder);
        model.addAttribute("orders", ListOrder);
        model.addAttribute("OrderFilter", orderFilter);
        return "admin/ListOrders";
    }

    @GetMapping("/OrderDetail/{id}")
    public String ListOrders(Model model, HttpServletRequest request, @PathVariable("id") Long id) {
        Order order = orderService.findOrderById(id);
        List<OrderDetail> orderDetails = orderDetailService.findOrderDetailByOrderId(id);
        Date dateTime = new Date(order.getOrderDate().getTime());

        String formatDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(order.getOrderDate());
        order.setOrderDateFormat(formatDate);

        String message = (String) request.getSession().getAttribute("noti_message");
        request.getSession().setAttribute("noti_message", null);
        model.addAttribute("noti_message", message);
        User user = (User) request.getSession().getAttribute("session_admin");
        model.addAttribute("user", user);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("Order", order);
        model.addAttribute("dateTime", dateTime);

        return "admin/OrderDetail";
    }

    @PostMapping("/ChangeStatus")
    public String ChangeStatus(Model model, HttpServletRequest request, @ModelAttribute("Order") Order order, @ModelAttribute("ID") Long id) {
        Order orderDb = orderService.findOrderById(id);
        orderDb.setStatus(order.getStatus());
        orderService.update(orderDb);
        request.getSession().setAttribute("noti_message", "changed status order successfully");
        return "redirect:/admin/OrderDetail/" + id;

    }
}
