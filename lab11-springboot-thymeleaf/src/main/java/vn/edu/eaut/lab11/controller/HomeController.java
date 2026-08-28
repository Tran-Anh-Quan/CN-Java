package vn.edu.eaut.lab11.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Trang Chủ - Lab 11 Spring Boot & Thymeleaf");
        model.addAttribute("welcomeMessage", "Chào mừng bạn đến với Ứng dụng Lab 11!");
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "Giới Thiệu - Lab 11");
        model.addAttribute("appName", "Spring Boot & Thymeleaf Web Demo");
        model.addAttribute("author", "Trần Anh Quân");
        model.addAttribute("description", "Ứng dụng được xây dựng theo kiến trúc MVC chuẩn Spring Boot kết hợp giao diện Thymeleaf hiện đại.");
        return "about";
    }
}
