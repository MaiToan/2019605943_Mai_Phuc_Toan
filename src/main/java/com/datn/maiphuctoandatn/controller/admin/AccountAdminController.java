package com.datn.maiphuctoandatn.controller.admin;

import com.datn.maiphuctoandatn.config.FileUploadUtil;
import com.datn.maiphuctoandatn.model.User;
import com.datn.maiphuctoandatn.service.face.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Controller
@RequestMapping("admin")
public class AccountAdminController {

    @Autowired
    IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/change-password")
    public String ChangePass(Model model, HttpServletRequest request) {
        String message = (String) request.getSession().getAttribute("noti_message");
        request.getSession().setAttribute("noti_message", null);
        model.addAttribute("noti_message", message);
        User user = (User) request.getSession().getAttribute("session_admin");
        model.addAttribute("user", user);
        return "admin/ChangePassword";
    }

    @PostMapping("/change-password")
    public String UpdatePass(@ModelAttribute("password-old") String password, @ModelAttribute("password-new") String passNew,
                             @ModelAttribute("repeat-pass") String reatPass, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("session_admin");
        Boolean checkPass = userService.checkPassword(password, user.getPassword());
        if (checkPass == false) {
            request.getSession().setAttribute("noti_message", "Enter wrong password !");
            return "redirect:/admin/change-password";
        }
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        if (!passNew.matches(pattern)){
            request.getSession().setAttribute("noti_message", "Password need to have 8 or more characters with a mix of letters numbers and symbols");
            return "redirect:/admin/change-password";
        }
        if (!passNew.equals(reatPass)) {
            request.getSession().setAttribute("noti_message", "The re-entered password does not match");
            return "redirect:/admin/change-password";
        }
        User checkSuc = userService.changePass(user, passwordEncoder.encode(passNew));
        if (checkSuc == null) {
            request.getSession().setAttribute("noti_message", "Password change failed");
            return "redirect:/admin/change-password";
        }
        request.getSession().setAttribute("noti_message", "changed password successfully");
        return "redirect:/admin/change-password";
    }

    @GetMapping("/account-admin")
    public String AccountAdmin(Model model, HttpServletResponse response , HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("session_admin");
        if (user == null) {
            request.getSession().setAttribute("noti_message", "Log in before performing this function");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
            }
            return "redirect:/login";
        }
        String message = (String) request.getSession().getAttribute("noti_message");
        request.getSession().setAttribute("noti_message", null);
        model.addAttribute("noti_message", message);
        model.addAttribute("user", user);
        return "admin/ProfileAdmin";
    }

    @PostMapping("/account-admin")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam("image") MultipartFile multipartFile, HttpServletRequest request
    ) throws IOException {
        String CheckFile = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String fileName = "";
        if (!CheckFile.isEmpty()) {
            fileName = String.valueOf(new Date().getTime()) + ".jpg";
            user.setAvatar(fileName);
        }
        User userUpdate = userService.updateUser(user);
        if (!CheckFile.isEmpty()) {
            String uploadDir = "src/main/upload/static/images/gallery";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        }
        request.getSession().setAttribute("session_admin", userUpdate);
        request.getSession().setAttribute("noti_message", "Update information successful");
        return "redirect:/admin/account-admin";

    }
}
