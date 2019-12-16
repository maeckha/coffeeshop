package de.htwg.swqs.shopui.controller;

import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping(value = {"admin/adminhome"})
    public String about(Model model) {
        return "admin/adminhome";
    }
}
