package com.datn.maiphuctoandatn.controller.admin;

import com.datn.maiphuctoandatn.model.Post;
import com.datn.maiphuctoandatn.model.Store;
import com.datn.maiphuctoandatn.model.User;
import com.datn.maiphuctoandatn.service.face.IStoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("admin")
public class StoreController {

    @Autowired
    IStoreService storeService;

    @GetMapping("/Store")
    public String store(Model model, HttpServletRequest request) {
        Store store= storeService.findStore();
        User user = (User) request.getSession().getAttribute("session_admin");
        model.addAttribute("user", user);
        model.addAttribute("store", store);
        return "admin/Store";
    }
}
