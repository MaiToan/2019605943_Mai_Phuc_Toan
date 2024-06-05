package com.datn.maiphuctoandatn.controller.admin;

import com.datn.maiphuctoandatn.config.FileUploadUtil;
import com.datn.maiphuctoandatn.model.*;
import com.datn.maiphuctoandatn.service.face.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/admin")
public class ManageProductController {

    @Autowired
    IUserService userService;

    @Autowired
    IProductService productService;

    @Autowired
    ICommetService commetService;

    @Autowired
    IAuthorService authorService;

    @Autowired
    ICategoryService categoryService;

    @GetMapping("/ListProducts")
    public String ListProducts(Model model, HttpServletRequest request, @ModelAttribute("searchProduct") String searchProduct) {
        List<Product> ListProducts = new ArrayList<>();
        if ((Objects.equals(searchProduct, null) ||
                Objects.equals(searchProduct, ""))) {
            ListProducts = productService.GetAllProduct();
        } else {
            ListProducts = productService.findProductBySearch(searchProduct);
        }
        for (Product product : ListProducts) {
            String formatDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(product.getCreated_at());
            product.setCreateFormatDate(formatDate);
        }
        model.addAttribute("searchProduct", searchProduct);
        model.addAttribute("products", ListProducts);
        return "admin/ListProducts";
    }

    @GetMapping("/DetailProduct/{id}")
    public String DetailProduct(Model model, HttpServletRequest request, @PathVariable("id") Long id,  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Product product = productService.getProductById(id);
        request.getSession().setAttribute("id_pro", id);
        List<Comment> comments = commetService.GetCommentList(id);
        List<ProductAuthor> authors = product.getProductAuthors();

        Page<Comment> lsComments = commetService.getCommentByProduct(id, pageNo, 3);
        int One = 0;
        int Two = 0;
        int Three = 0;
        int Four = 0;
        int Five = 0;
        Double AvgRate = 0.0;
        for (Comment item : comments) {
            AvgRate += item.getRateNumber();
            switch (item.getRateNumber()) {
                case 1:
                    One += 1;
                    break;
                case 2:
                    Two += 1;
                    break;
                case 3:
                    Three += 1;
                    break;
                case 4:
                    Four += 1;
                    break;
                case 5:
                    Five += 1;
                    break;
            }
        }
        if (!comments.isEmpty())
            AvgRate /= comments.size();

        model.addAttribute("product", product);
        model.addAttribute("comments", comments);
        model.addAttribute("authors", authors);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("lsComment", lsComments);
        model.addAttribute("TotalPages", lsComments.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("avgRate", AvgRate);
        model.addAttribute("One", One);
        model.addAttribute("Two", Two);
        model.addAttribute("Three", Three);
        model.addAttribute("Four", Four);
        model.addAttribute("Five", Five);
        return "admin/DetailProduct";
    }

    @PostMapping("/delete-product")
    public String DeleteProduct(Model model, @ModelAttribute("id") Long id) {
        Product product = productService.getProductById(id);
        product.setLoevm("1");
        productService.deleteProduct(product);
        return "redirect:/admin/ListProducts";
    }

    @GetMapping("/edit-product/{id}")
    public String EditProduct(Model model, HttpServletRequest request, @PathVariable("id") Long id) {
        Product product = productService.getProductById(id);
        List<Categories> categoriesList = categoryService.getAll();
        List<Author> authorList = authorService.getAllAuthors();
        Integer check = (Integer) request.getSession().getAttribute("check");
        request.getSession().setAttribute("check", null);
        List<Author> authorCard = new ArrayList<>();
        if (!Objects.equals(check, 1)) {
            request.getSession().setAttribute("author-product", null);
            List<ProductAuthor> productAuthors = product.getProductAuthors();
            for (ProductAuthor productAuthor : productAuthors) {
                authorCard.add(productAuthor.getAuthorProduct());
            }
            request.getSession().setAttribute("author-product", authorCard);
        } else {
            authorCard = (List<Author>) request.getSession().getAttribute("author-product");
        }

        if (authorCard == null) {
            authorCard = new ArrayList<>();
        }
        model.addAttribute("product", product);
        model.addAttribute("categoriesList", categoriesList);
        model.addAttribute("authorList", authorList);
        model.addAttribute("authorCard", authorCard);
        return "admin/EditProduct";
    }

    @PostMapping("/choose-author")
    public String ChooseAuthor(Model model, HttpServletRequest request, @ModelAttribute("choose-author") Long id, @ModelAttribute("id_product") Long id_product) {
        List<Author> authorList = (List<Author>) request.getSession().getAttribute("author-product");
        if (authorList == null) {
            authorList = new ArrayList<>();
        }
        request.getSession().setAttribute("check", 1);
        Author author = authorService.getAuthorById(id);
        for (Author item : authorList) {
            if (Objects.equals(item.getId(), author.getId())) {
                return "redirect:/admin/edit-product/" + id_product;
            }
        }
        authorList.add(author);
        request.getSession().setAttribute("author-product", authorList);
        return "redirect:/admin/edit-product/" + id_product;
    }

    @PostMapping("/choose-author-add")
    public String ChooseAuthorAdd(Model model, HttpServletRequest request, @ModelAttribute("choose-author") Long id) {
        List<Author> authorList = (List<Author>) request.getSession().getAttribute("author-product");
        if (authorList == null) {
            authorList = new ArrayList<>();
        }
        request.getSession().setAttribute("check", 1);
        Author author = authorService.getAuthorById(id);
        for (Author item : authorList) {
            if (Objects.equals(item.getId(), author.getId())) {
                return "redirect:/admin/NewProduct";
            }
        }
        authorList.add(author);
        request.getSession().setAttribute("author-product", authorList);
        return "redirect:/admin/NewProduct";
    }

    @PostMapping("/remove-author")
    public String RemoveAuthor(Model model, HttpServletRequest request, @ModelAttribute("id_author") Long id_author, @ModelAttribute("id_product") Long id_product) {
        List<Author> authorList = (List<Author>) request.getSession().getAttribute("author-product");
        if (authorList == null) {
            authorList = new ArrayList<>();
        }
        Author author = authorService.getAuthorById(id_author);
        authorList.removeIf(item -> Objects.equals(item.getId(), author.getId()));
        request.getSession().setAttribute("author-product", authorList);
        request.getSession().setAttribute("check", 1);
        return "redirect:/admin/edit-product/" + id_product;
    }

    @PostMapping("/remove-author-add")
    public String RemoveAuthorAdd(Model model, HttpServletRequest request, @ModelAttribute("id_author") Long id_author) {
        List<Author> authorList = (List<Author>) request.getSession().getAttribute("author-product");
        if (authorList == null) {
            authorList = new ArrayList<>();
        }
        Author author = authorService.getAuthorById(id_author);
        authorList.removeIf(item -> Objects.equals(item.getId(), author.getId()));
        request.getSession().setAttribute("author-product", authorList);
        request.getSession().setAttribute("check", 1);
        return "redirect:/admin/NewProduct";
    }

    @PostMapping("/update-product")
    public String UpdateProduct(@ModelAttribute("product") Product product, @RequestParam("image_product") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        String CheckFile = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String fileName = "";
        if (!CheckFile.isEmpty()) {
            fileName = String.valueOf(new Date().getTime()) + ".jpg";
            product.setImage(fileName);
        }
        List<Author> authorList = (List<Author>) request.getSession().getAttribute("author-product");
        productService.updateProduct(product, authorList);
        if (!CheckFile.isEmpty()) {
            String uploadDir = "src/main/upload/static/images/gallery";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        }
        return "redirect:/admin/DetailProduct/" + product.getId();
    }
    @GetMapping("/NewProduct")
    public String AddProduct(Model model, HttpServletRequest request) {
        Product product = new Product();
        List<Categories> categoriesList = categoryService.getAll();
        List<Author> authorList = authorService.getAllAuthors();
        Integer check = (Integer) request.getSession().getAttribute("check");
        request.getSession().setAttribute("check", null);
        List<Author> authorCard = new ArrayList<>();
        if (!Objects.equals(check, 1)) {
            request.getSession().setAttribute("author-product", null);
        } else {
            authorCard = (List<Author>) request.getSession().getAttribute("author-product");
        }

        if (authorCard == null) {
            authorCard = new ArrayList<>();
        }
        model.addAttribute("categoriesList", categoriesList);
        model.addAttribute("authorList", authorList);
        model.addAttribute("authorCard", authorCard);
        model.addAttribute("product", product);
        return "admin/AddProduct";
    }

    @PostMapping("/add-product")
    public String AddProduct(@ModelAttribute("product") Product product, @RequestParam("image_product") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        String CheckFile = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String fileName = "";
        if (!CheckFile.isEmpty()) {
            fileName = String.valueOf(new Date().getTime()) + ".jpg";
            product.setImage(fileName);
        }
        List<Author> authorList = (List<Author>) request.getSession().getAttribute("author-product");
        product.setProductNumber(0);
        product.setLoevm("0");
        product.setProductSold(0);
        productService.saveProduct(product, authorList);
        if (!CheckFile.isEmpty()) {
            String uploadDir = "src/main/upload/static/images/gallery";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        }
        return "redirect:/admin/ListProducts";
    }

    @GetMapping("/handler-top-comment/{id}")
    public String HandlerTopComment(Model model, HttpServletRequest request, @PathVariable("id") Long id) {
       Comment comment = commetService.getCommentByID(id);
       if (comment.getTopComment() == 1)
           comment.setTopComment(0);
       else comment.setTopComment(1);
       commetService.saveData(comment);
       Long id_pro = (Long) request.getSession().getAttribute("id_pro");
        return "redirect:/admin/DetailProduct/"+id_pro;
    }

}
