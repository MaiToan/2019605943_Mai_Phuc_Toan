package com.datn.maiphuctoandatn.controller.user;

import com.datn.maiphuctoandatn.model.*;
import com.datn.maiphuctoandatn.service.cls.ProductService;
import com.datn.maiphuctoandatn.service.face.ICategoryService;
import com.datn.maiphuctoandatn.service.face.IStoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {

    @Autowired
    IStoreService storeService;

    @Autowired
    ICategoryService categoryService;

    private final ProductService productService;

    public CartController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/cart")
    public String cart(Model model, HttpServletRequest request) {
        HashMap<Long, SessionCart> HashCart = (HashMap<Long, SessionCart>) request.getSession().getAttribute("myCartItems");
        Double myCartTotal = (Double) request.getSession().getAttribute("myCartTotal");
        User user = (User) request.getSession().getAttribute("session_user");
        if (user == null) {
            user = new User();
        }
        String message = (String) request.getSession().getAttribute("noti_message");
        request.getSession().setAttribute("noti_message", null);
        Order sessionOrder = new Order();
        Store store = storeService.findStore();
        Order OrderGet = (Order) request.getSession().getAttribute("session_order");
        List<Categories> categories = categoryService.getThreeCategories();

        if (OrderGet == null && user != null) {
            sessionOrder.setName(user.getName());
            sessionOrder.setPhone(user.getPhone());
            sessionOrder.setAddress(user.getAddress());
        }
        if (OrderGet != null) {
            sessionOrder = OrderGet;
        }
        List<SessionCart> lsCart = new ArrayList<>();

        if (HashCart != null) {
            for (SessionCart item : HashCart.values()) {
                String price = String.format("%.1f", item.getProduct().getPrice() - item.getProduct().getSale());
                item.getProduct().setUnitPrice(price);
                lsCart.add(item);
            }
        }
        model.addAttribute("store", store);
        model.addAttribute("cart", lsCart);
        model.addAttribute("myCartTotal", myCartTotal);
        model.addAttribute("user", user);
        model.addAttribute("order_info", sessionOrder);
        model.addAttribute("noti_message", message);
        model.addAttribute("categories", categories);

        return "user/Cart";
    }

    @PostMapping("/cart")
    public String AddCart(Model model, HttpServletRequest request, @ModelAttribute("AddCart") Long productId, @ModelAttribute("Number") Integer number, @ModelAttribute("URL") String url) {
        HashMap<Long, SessionCart> cartItems = (HashMap<Long, SessionCart>) request.getSession().getAttribute("myCartItems");
        if (cartItems == null) {
            cartItems = new HashMap<>();
        }
        Product product = productService.getProductById(productId);
        if (number > product.ProductNumber) {
            request.getSession().setAttribute("noti_message", "Adding book to cart failed because number add more than stock product");
            String http = "redirect:" + url;
            return http;
        }
        if (product != null) {
            if (cartItems.containsKey(productId)) {
                SessionCart item = cartItems.get(productId);
                item.setProduct(product);
                item.setQuantity(item.getQuantity() + number);
                cartItems.put(productId, item);
            } else {
                SessionCart item = new SessionCart();
                item.setProduct(product);
                item.setQuantity(number);
                cartItems.put(productId, item);
            }
            request.getSession().setAttribute("myCartItems", cartItems);
            request.getSession().setAttribute("myCartTotal", totalPrice(cartItems));
            request.getSession().setAttribute("myCartNum", cartItems.size());
            request.getSession().setAttribute("noti_message", "Added book to cart successfully");
        } else
            request.getSession().setAttribute("noti_message", "Adding book to cart failed");
        String http = "redirect:" + url;
        return http;

    }

    @PostMapping("/update-quantity")
    public String UpdateQuantity(Model model, HttpServletRequest request, @ModelAttribute("num-product1") Integer number, @ModelAttribute("id_product") Long productId) {
        HashMap<Long, SessionCart> cartItems = (HashMap<Long, SessionCart>) request.getSession().getAttribute("myCartItems");
        if (cartItems != null) {
            if (cartItems.containsKey(productId)) {
                SessionCart item = cartItems.get(productId);
                item.setQuantity(number);
                cartItems.put(productId, item);
            }

            request.getSession().setAttribute("myCartItems", cartItems);
            request.getSession().setAttribute("myCartTotal", totalPrice(cartItems));
            request.getSession().setAttribute("myCartNum", cartItems.size());
            request.getSession().setAttribute("noti_message", "Added book to cart successfully");
        } else
            request.getSession().setAttribute("noti_message", "Update quantity in cart successfully");
        return "redirect:/cart";
    }

    @PostMapping("/update-cart")
    public String UpdateInfomation(Model model, HttpServletRequest request, @ModelAttribute("order_info") Order order) {
        request.getSession().setAttribute("session_order", order);
        request.getSession().setAttribute("noti_message", "Order information has been updated");
        return "redirect:/cart";
    }

    @GetMapping("remove-cart")
    public String viewRemove(HttpServletRequest request, @RequestParam("product_id") long productId) {
        HashMap<Long, SessionCart> cartItems = (HashMap<Long, SessionCart>) request.getSession().getAttribute("myCartItems");
        if (cartItems == null) {
            cartItems = new HashMap<>();
        }
        if (cartItems.containsKey(productId)) {
            cartItems.remove(productId);
        }
        request.getSession().setAttribute("myCartItems", cartItems);
        request.getSession().setAttribute("myCartTotal", totalPrice(cartItems));
        request.getSession().setAttribute("myCartNum", cartItems.size());
        request.getSession().setAttribute("noti_message", "Product has been removed from the cart");
        return "redirect:/cart";
    }

    public Double totalPrice(HashMap<Long, SessionCart> cartItems) {
        double count = 0;
        for (Map.Entry<Long, SessionCart> list : cartItems.entrySet()) {
            count += (list.getValue().getProduct().Price - list.getValue().getProduct().Sale) * list.getValue().getQuantity();
        }
        return count;
    }
}
