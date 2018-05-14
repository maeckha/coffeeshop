package de.htwg.swqs.shopui.integration;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import de.htwg.swqs.catalog.model.Product;
import de.htwg.swqs.catalog.repository.CatalogRepository;
import de.htwg.swqs.catalog.service.CatalogServiceImpl;
import de.htwg.swqs.shopui.HelperUtil;
import de.htwg.swqs.shopui.controller.CatalogController;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Tests the interaction between the CatalogController and the CatalogService. For verifying the
 * results we use Hamcrest matchers.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class CatalogIT {

  @Autowired
  CatalogRepository catalogRepository;

  private MockMvc mvc;

  @Test
  public void retrieveAllProducts() throws Exception {

    // setup
    this.catalogRepository.deleteAll();
    Product dummyProductOne = HelperUtil.createDummyProduct(1L);
    this.catalogRepository.saveAndFlush(dummyProductOne);

    Product dummyProductTwo = HelperUtil.createDummyProduct(2L);
    this.catalogRepository.saveAndFlush(dummyProductTwo);

    CatalogController catalogController = new CatalogController(
        new CatalogServiceImpl(this.catalogRepository));

    this.mvc = MockMvcBuilders.standaloneSetup(catalogController).build();

    // execute & verify
    this.mvc.perform(get("/products")
        .accept(MediaType.TEXT_HTML))
        .andExpect(model().attributeExists("title", "products"))
        .andExpect(model().attribute("products", hasSize(2)))
        .andExpect(model().attribute("products", hasItem(
            allOf(
                hasProperty("id", is(1L)),
                hasProperty("description", is("sample description")),
                hasProperty("name", is("sample product")),
                hasProperty("priceEuro", is(new BigDecimal("3.14")))
            )
        )))
        .andExpect(model().attribute("products", hasItem(
            allOf(
                hasProperty("id", is(2L)),
                hasProperty("description", is("sample description")),
                hasProperty("name", is("sample product")),
                hasProperty("priceEuro", is(new BigDecimal("3.14")))
            )
        )))
        .andExpect(view().name("catalog"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  // todo: write test for retrieving just a single product by id

}
