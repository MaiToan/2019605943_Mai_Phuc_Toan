package com.datn.maiphuctoandatn.controller.authen;

import com.datn.maiphuctoandatn.model.User;
import com.datn.maiphuctoandatn.service.face.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @Autowired
    IUserService userService;

    @GetMapping("/login")
    public String Login(Model model, HttpServletRequest request) {
        String message = (String) request.getSession().getAttribute("noti_message");
        request.getSession().setAttribute("noti_message", null);
        model.addAttribute("noti_message", message);
        model.addAttribute("user", new User());
        return "/auth/Login";
    }
}
