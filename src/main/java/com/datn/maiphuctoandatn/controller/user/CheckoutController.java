package com.datn.maiphuctoandatn.controller.user;

import com.datn.maiphuctoandatn.model.*;
import com.datn.maiphuctoandatn.service.cls.ProductService;
import com.datn.maiphuctoandatn.service.face.ICategoryService;
import com.datn.maiphuctoandatn.service.face.IOrderService;
import com.datn.maiphuctoandatn.service.face.IProductService;
import com.datn.maiphuctoandatn.service.face.IStoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class CheckoutController {

    @Autowired
    IOrderService orderService;

    @Autowired
    IStoreService storeService;

    @Autowired
    ICategoryService categoryService;
    @Autowired
    private IProductService productService;

    @PostMapping("/checkout")
    public String Checkout(Model model, HttpServletRequest request) {
        HashMap<Long, SessionCart> HashCart = (HashMap<Long, SessionCart>) request.getSession().getAttribute("myCartItems");
        Double myCartTotal = (Double) request.getSession().getAttribute("myCartTotal");
        User user = (User) request.getSession().getAttribute("session_user");
        Store store = storeService.findStore();
        Order OrderGet = (Order) request.getSession().getAttribute("session_order");
        List<Categories> categories = categoryService.getThreeCategories();

        if (user == null){
            request.getSession().setAttribute("noti_message", "You need login before you can do this!");
            return "redirect:/cart";
        }
        if (OrderGet == null) {
            request.getSession().setAttribute("noti_message", "Need to provide complete information !");
            return "redirect:/cart";
        }
        if (OrderGet.Name == null || OrderGet.Address == null || OrderGet.Country == null ||
                OrderGet.City == null || OrderGet.District == null || OrderGet.Phone == null) {
            request.getSession().setAttribute("noti_message", "Need to provide complete information !");
            return "redirect:/cart";
        }
        if (myCartTotal == null || myCartTotal <= 0){
            request.getSession().setAttribute("noti_message", "There are no products to order yet !");
            return "redirect:/cart";
        }
        List<SessionCart> lsCart = new ArrayList<>();

        if (HashCart != null) {
            for (SessionCart item : HashCart.values()) {
                lsCart.add(item);
            }
        }
        model.addAttribute("categories", categories);
        model.addAttribute("store", store);
        model.addAttribute("cart", lsCart);
        model.addAttribute("myCartTotal", myCartTotal);
        model.addAttribute("user", user);
        model.addAttribute("order_info", OrderGet);
        return "user/Checkout";
    }

    @PostMapping("/order")
    public String Order(Model model, HttpServletRequest request, @ModelAttribute("note") String note, @ModelAttribute("paymethod") Integer paymethod) {
        HashMap<Long, SessionCart> HashCart = (HashMap<Long, SessionCart>) request.getSession().getAttribute("myCartItems");
        Double myCartTotal = (Double) request.getSession().getAttribute("myCartTotal");
        User user = (User) request.getSession().getAttribute("session_user");
        Order OrderGet = (Order) request.getSession().getAttribute("session_order");
        List<SessionCart> lsCart = new ArrayList<>();

        if (HashCart != null) {
            for (SessionCart item : HashCart.values()) {
                lsCart.add(item);
            }
        }
        for (SessionCart item : lsCart) {
            Product  productUpdate = item.getProduct();
            productUpdate.ProductSold += item.getQuantity();
            productUpdate.ProductNumber -= item.getQuantity();
            productService.saveProduct(productUpdate);
        }
        OrderGet.setPaymentMethod(paymethod);
        OrderGet.setNote(note);
        OrderGet.setStatus(1);
        OrderGet.setDisCount(myCartTotal);
        OrderGet.setUser_order(user);
        OrderGet.setId(null);
        orderService.saveData(OrderGet, lsCart);
        request.getSession().setAttribute("method_payment", paymethod);
        request.getSession().setAttribute("myCartItems", null);
//        request.getSession().setAttribute("myCartTotal", null);
        request.getSession().setAttribute("myCartNum", null);
        return "redirect:/complete-order";
    }

    @GetMapping("/complete-order")
    public String complete(Model model, HttpServletRequest request) {
        Store store = storeService.findStore();
        List<Categories> categories = categoryService.getThreeCategories();

        Double myCartTotal = (Double) request.getSession().getAttribute("myCartTotal");
        request.getSession().setAttribute("myCartTotal", null);

        Integer paymentMethod = (Integer) request.getSession().getAttribute("method_payment");
        User user = (User) request.getSession().getAttribute("session_user");
        model.addAttribute("total", myCartTotal);
        model.addAttribute("store", store);
        model.addAttribute("payment_method", paymentMethod);
        model.addAttribute("user", user);
        model.addAttribute("categories", categories);

        return "user/OrderComplete";
    }
}
