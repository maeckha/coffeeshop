package de.htwg.swqs.shopui.controller;

import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


  @GetMapping(value = {"/", "", "home"})
  public String home(Model model) {
    model.addAttribute("title", "E-Commerce Shop | Home");
    return "index";
  }

  @GetMapping(value = {"about"})
  public String about(Model model) {
    model.addAttribute("title", "E-Commerce Shop | About");
    return "about";
  }
}
