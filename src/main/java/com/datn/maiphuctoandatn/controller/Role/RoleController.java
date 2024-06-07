package com.datn.maiphuctoandatn.controller.Role;

import com.datn.maiphuctoandatn.model.User;
import com.datn.maiphuctoandatn.service.face.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoleController {
@Autowired
    IUserService userService;

    @GetMapping("CheckRole")
    public String CheckRole(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.CheckEmail(email);
        if (user.getRole().equals("ROLE_ADMIN")) {
            request.getSession().setAttribute("session_admin", user);
            return "redirect:/admin/Home";
        }
        if (user.getActive() == 0){
            request.getSession().setAttribute("noti_message", "User has lock by admin");
            return "redirect:/login";
        }
        request.getSession().setAttribute("session_user", user);
        return "redirect:/Home";
    }
}
