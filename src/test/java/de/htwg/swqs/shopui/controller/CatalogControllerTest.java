package de.htwg.swqs.shopui.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import de.htwg.swqs.catalog.model.Product;
import de.htwg.swqs.catalog.service.CatalogService;
import de.htwg.swqs.catalog.utils.ProductNotFoundException;
import de.htwg.swqs.shopui.HelperUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

/**
 * In this test suite we only test the CartViewController, the DOC are just a mock. We don't use the
 * html sites for navigation, just the controller methods.
 */
public class CatalogControllerTest {

  private MockMvc mvc;
  private CatalogService catalogService;
  private CatalogController catalogController;
  private List<Product> productList;

  @Before
  public void setUp() {
    this.catalogService = mock(CatalogService.class);
    this.catalogController = new CatalogController(this.catalogService);
    this.productList = new ArrayList<>();
    this.productList.add(HelperUtil.createDummyProduct(10001L));
    this.productList.add(HelperUtil.createDummyProduct(10002L));
    this.productList.add(HelperUtil.createDummyProduct(10003L));
    this.mvc = MockMvcBuilders.standaloneSetup(this.catalogController).build();

    when(this.catalogService.getAllProducts()).thenReturn(this.productList);
    when(this.catalogService.getProductById(this.productList.get(0).getId()))
        .thenReturn(this.productList.get(0));

  }

  @Test
  public void getAllProducts() throws Exception {

    // execute & verify
    this.mvc.perform(get("/products")
        .accept(MediaType.TEXT_HTML))
        .andExpect(model()
            .attributeExists("title", "products"))
        .andExpect(view().name("catalog"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("products", hasItem(
            allOf(
                hasProperty("id", is(this.productList.get(0).getId())),
                hasProperty("description", is(this.productList.get(0).getDescription())),
                hasProperty("name", is(this.productList.get(0).getName())),
                hasProperty("priceEuro", is(this.productList.get(0).getPriceEuro()))
            )
        )))
        .andExpect(model().attribute("products", hasItem(
            allOf(
                hasProperty("id", is(this.productList.get(1).getId())),
                hasProperty("description", is(this.productList.get(1).getDescription())),
                hasProperty("name", is(this.productList.get(1).getName())),
                hasProperty("priceEuro", is(this.productList.get(1).getPriceEuro()))
            )
        )))
        .andExpect(model().attribute("products", hasItem(
            allOf(
                hasProperty("id", is(this.productList.get(2).getId())),
                hasProperty("description", is(this.productList.get(2).getDescription())),
                hasProperty("name", is(this.productList.get(2).getName())),
                hasProperty("priceEuro", is(this.productList.get(2).getPriceEuro()))
            )
        )))
        .andDo(print());
  }


  @Test
  public void getProductById() throws Exception {

    // execute & verify
    this.mvc.perform(get("/products/" + this.productList.get(0).getId())
        .accept(MediaType.TEXT_HTML))
        .andExpect(model()
            .attributeExists("title", "product"))
        .andExpect(view().name("detail"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("product", allOf(
            hasProperty("id", is(this.productList.get(0).getId())),
            hasProperty("description", is(this.productList.get(0).getDescription())),
            hasProperty("name", is(this.productList.get(0).getName())),
            hasProperty("priceEuro", is(this.productList.get(0).getPriceEuro()))
            )
        ))
        .andDo(print());
  }

  @Test(expected = ProductNotFoundException.class)
  public void getProductByIdThatDoesntExists() throws Throwable {

    when(this.catalogService.getProductById(999999)).thenThrow(ProductNotFoundException.class);

    // execute & verify
    try {
      this.mvc.perform(get("/products/" + 999999)
          .accept(MediaType.TEXT_HTML))
          .andDo(print());

    } catch (NestedServletException exc) {
      // in spring web mvc the exception is nested
      throw exc.getCause();
    }
  }
}
