package com.datn.maiphuctoandatn.controller.authen;

import com.datn.maiphuctoandatn.model.User;
import com.datn.maiphuctoandatn.service.face.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {
    @Autowired
    IUserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String Register(Model model) {
        model.addAttribute("user", new User());
        return "auth/Register";
    }

    @PostMapping("/register")
    public String RegisterPost(Model model, @ModelAttribute("user") User user, @RequestParam("Repassword") String pass) {
        User user1 = userService.CheckEmail(user.Email);
        if (user1 != null) {
            model.addAttribute("noti_message", "Registration failed because the user already exists");
            return "auth/Register";
        } else {
            String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
            if (!user.getPassword().matches(pattern)){
                model.addAttribute("noti_message", "Password need to have 8 or more characters with a mix of letters numbers and symbols");
                return "auth/Register";
            }
            if (!pass.equals(user.Password) || pass.equals("")) {
                model.addAttribute("noti_message", "Registration failed due to incorrect password confirmation");
                return "auth/Register";
            }
            user.setRole("ROLE_USER");
            user.setActive(1);
            user.setPassword(passwordEncoder.encode(pass));
            userService.SaveData(user);
            return "redirect:/login";
        }

    }
}
