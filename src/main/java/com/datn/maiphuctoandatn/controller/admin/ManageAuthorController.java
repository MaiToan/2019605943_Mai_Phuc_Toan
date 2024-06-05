package com.datn.maiphuctoandatn.controller.admin;

import com.datn.maiphuctoandatn.config.FileUploadUtil;
import com.datn.maiphuctoandatn.model.Author;
import com.datn.maiphuctoandatn.model.Product;
import com.datn.maiphuctoandatn.service.cls.ProductService;
import com.datn.maiphuctoandatn.service.face.IAuthorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("admin")
public class ManageAuthorController {

    @Autowired
    IAuthorService authorService;
    @Autowired
    private ProductService productService;

    @GetMapping("/ListAuthor")
    public String ListAuthor(Model model, HttpServletRequest request, @ModelAttribute("searchAuthor") String searchAuthor) {
        List<Author> ListAuthor = new ArrayList<>();
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

    @GetMapping("/NewAuthor")
    public String NewAuthor(Model model) {
        model.addAttribute("author", new Author());
        return "admin/AddAuthor";
    }

    @PostMapping("/add-author")
    public String AddAuthor(@ModelAttribute("author") Author author, @RequestParam("image_author") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        String CheckFile = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String fileName = "";
        if (!CheckFile.isEmpty()) {
            fileName = String.valueOf(new Date().getTime()) + ".jpg";
            author.setAvatar(fileName);
        }
        author.setLOEVM("0");
        authorService.saveAuthor(author);
        if (!CheckFile.isEmpty()) {
            String uploadDir = "src/main/upload/static/images/gallery";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        }
        return "redirect:/admin/ListAuthor";
    }

    @GetMapping("/detail-author/{id}")
    public String detailAuthor(Model model, HttpServletRequest request, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                               @ModelAttribute("keyword") String name, @ModelAttribute("sort") String option_sort, @PathVariable("id") Long id) {
        Author author = authorService.getAuthorById(id);
        Page<Product> LsProduct = productService.getAllProductByAuthor(id, option_sort, name, pageNo, 15);

        model.addAttribute("curentPage", pageNo);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("TotalPages", LsProduct.getTotalPages());
        model.addAttribute("option_sort", option_sort);
        model.addAttribute("keyword", name);
        model.addAttribute("author", author);
        model.addAttribute("LsProduct", LsProduct);

        return "admin/DetailAuthor";
    }

    @PostMapping("/delete-author")
    public String DeleteAuthor(@ModelAttribute("id") Long id) {
        Author author = authorService.getAuthorById(id);
        author.setLOEVM("1");
        authorService.deleteAuthor(author);
        return "redirect:/admin/ListAuthor";
    }

    @GetMapping("/edit-author/{id}")
    public String editAuthor(Model model, @PathVariable("id") Long id) {
        Author author = authorService.getAuthorById(id);
        model.addAttribute("author", author);
        return "/admin/EditAuthor";
    }

    @PostMapping("edit-author")
    public String EditAuthor(@ModelAttribute("author") Author author, @RequestParam("image_author") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        String CheckFile = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String fileName = "";
        if (!CheckFile.isEmpty()) {
            fileName = String.valueOf(new Date().getTime()) + ".jpg";
            author.setAvatar(fileName);
        }
        authorService.updateAuthor(author);
        if (!CheckFile.isEmpty()) {
            String uploadDir = "src/main/upload/static/images/gallery";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        }
        return "redirect:/admin/detail-author/" + author.getId();
    }
}
