package com.datn.maiphuctoandatn.controller.admin;

import com.datn.maiphuctoandatn.model.Author;
import com.datn.maiphuctoandatn.service.face.IAuthorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("admin")
public class ManageAuthorController {
    
    @Autowired
    IAuthorService authorService;
    
    @GetMapping("/ListAuthor")
    public String ListAuthor(Model model, HttpServletRequest request, @ModelAttribute("searchAuthor") String searchAuthor) {
        List<Author> ListAuthor = new ArrayList<>() ;
        if ((Objects.equals(searchAuthor, null) ||
                Objects.equals(searchAuthor, ""))) {
            ListAuthor = authorService.getAllAuthors();
        } else {
            ListAuthor = authorService.findAuthorBySearch(searchAuthor);
        }
        model.addAttribute("searchAuthor", searchAuthor);
        model.addAttribute("authors", ListAuthor);
        return "admin/ListAuthor";
    }
}
