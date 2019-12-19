package de.htwg.swqs.shopui.controller;

import de.htwg.swqs.catalog.model.Product;
import de.htwg.swqs.catalog.service.CatalogService;
import de.htwg.swqs.catalog.utils.ProductNotFoundException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/products")
public class CatalogController {

  private CatalogService catalogService;

  @Autowired
  public CatalogController(CatalogService catalogService) {
    this.catalogService = catalogService;
  }

  @GetMapping(value = {"/", ""})
  public String getAllProducts(Model model) {
    model.addAttribute("title", "E-Commerce Shop | Product Catalog");
    List<Product> products = this.catalogService.getAllProducts();
    model.addAttribute("products", products);
    return "catalog";
  }

  @GetMapping(value = "/{id}")
  public String getProductById(@PathVariable long id, Model model, HttpServletResponse response) {
    Product product = this.catalogService.getProductById(id);
    model.addAttribute("title", "E-Commerce Shop | " + product.getName());
    model.addAttribute("product", product);
    response.encodeURL("/details/" + product.getId());
    return "detail";
  }

  @GetMapping(value = "/create")
  public String showCreateProductView(Model model){
    Product product = new Product();
    model.addAttribute("product", product);
    return "product-create";
  }

  @PostMapping(value = "/submit-product")
  public String submitProduct(Product product, Model model){
    this.catalogService.createProduct(product);
    return getAllProducts(model);
  }

}
