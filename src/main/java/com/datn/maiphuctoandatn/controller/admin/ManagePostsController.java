package com.datn.maiphuctoandatn.controller.admin;

import com.datn.maiphuctoandatn.config.FileUploadUtil;
import com.datn.maiphuctoandatn.model.Author;
import com.datn.maiphuctoandatn.model.Categories;
import com.datn.maiphuctoandatn.model.Post;
import com.datn.maiphuctoandatn.model.User;
import com.datn.maiphuctoandatn.service.cls.PostService;
import com.datn.maiphuctoandatn.service.face.IPostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("admin")
public class ManagePostsController {
    @Autowired
    IPostService postService;

    @GetMapping("/ListPosts")
    public String ListAuthor(Model model, HttpServletRequest request, @ModelAttribute("searchAuthor") String searchAuthor) {
        List<Post> posts = new ArrayList<>();
        posts = postService.getAll();

        for (Post post : posts) {
            String formatDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(post.getCreated_at());
            post.setCreated_at_format(formatDate);
        }
        String message = (String) request.getSession().getAttribute("noti_message");
        request.getSession().setAttribute("noti_message", null);
        model.addAttribute("noti_message", message);
        User user = (User) request.getSession().getAttribute("session_admin");
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        return "admin/ListPosts";
    }

    @GetMapping("/NewPost")
    public String NewPosts(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("session_admin");
        model.addAttribute("user", user);
        model.addAttribute("post", new Post());
        return "admin/AddPost";
    }

    @PostMapping("/add-post")
    public String AddAuthor(@ModelAttribute("post") Post post, @RequestParam("image_post") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        String CheckFile = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String fileName = "";
        if (!CheckFile.isEmpty()) {
            fileName = String.valueOf(new Date().getTime()) + ".jpg";
            post.setImage(fileName);
        }
        post.setLOEVM("0");
        postService.save(post);
        if (!CheckFile.isEmpty()) {
            String uploadDir = "src/main/upload/static/images/gallery";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        }
        request.getSession().setAttribute("noti_message", "add new post successfully");
        return "redirect:/admin/ListPosts";
    }

    @PostMapping("/delete-post")
    public String DeletePost(Model model, @ModelAttribute("id") Long id ,HttpServletRequest request) {
       postService.delete(id);
        request.getSession().setAttribute("noti_message", "delete post successfully");
        return "redirect:/admin/ListPosts";
    }
}
