package de.htwg.swqs.shopui.controller;

import de.htwg.swqs.catalog.model.Product;
import de.htwg.swqs.catalog.service.CatalogService;
import de.htwg.swqs.catalog.utils.ProductNotFoundException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/products")
public class CatalogController {

  private CatalogService catalogService;

  @Autowired
  public CatalogController(CatalogService catalogService) {
    this.catalogService = catalogService;
  }

  @RequestMapping(value = {"/", ""})
  public String getAllProducts(Model model) {
    model.addAttribute("title", "E-Commerce Shop | Product Catalog");
    try {
      List<Product> products = this.catalogService.getAllProducts();
      model.addAttribute("products", products);
      for (Product p : products) {
        System.out.println(p.toString());
      }
    } catch (ProductNotFoundException exc) {
      System.err.println("Exception thrown: " + exc.getMessage());
    }

    return "catalog";
  }

  @RequestMapping(value = "/{id}")
  public String getProductById(@PathVariable long id, Model model, HttpServletResponse response) {
    Product product = this.catalogService.getProductById(id);
    model.addAttribute("title", "E-Commerce Shop | " + product.getName());
    model.addAttribute("product", product);
    response.encodeURL("/details/" + product.getId());
    return "detail";
  }

}
