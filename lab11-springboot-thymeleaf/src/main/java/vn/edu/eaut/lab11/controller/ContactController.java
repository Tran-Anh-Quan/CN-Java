package vn.edu.eaut.lab11.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactController {

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("pageTitle", "Liên Hệ - Lab 11");
        model.addAttribute("facultyName", "Khoa Công Nghệ Thông Tin");
        model.addAttribute("department", "Bộ Môn Công Nghệ Phần Mềm");
        model.addAttribute("address", "Tầng 4, Tòa nhà A, Trường Đại học Công nghiệp Hà Nội");
        model.addAttribute("phone", "024.3855.1234");
        model.addAttribute("email", "cntt@haui.edu.vn");
        model.addAttribute("website", "https://haui.edu.vn");
        return "contact";
    }
}
