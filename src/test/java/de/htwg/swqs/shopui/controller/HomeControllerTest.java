package de.htwg.swqs.shopui.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class HomeControllerTest {

  private MockMvc mvc;

  private HomeController homeController;

  @Before
  public void setUp() {
    this.homeController = new HomeController();
    this.mvc = MockMvcBuilders.standaloneSetup(this.homeController).build();
  }

  @Test
  public void getStartPage() throws Exception {

    // execute & verify
    this.mvc.perform(get("/")
        .accept(MediaType.TEXT_HTML))
        .andExpect(model()
            .attributeExists("title"))
        .andExpect(view().name("index"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  public void getAboutPage() throws Exception {

    // execute & verify
    this.mvc.perform(get("/about.html")
        .accept(MediaType.TEXT_HTML))
        .andExpect(view().name("about"))
        .andExpect(status().isOk())
        .andDo(print());
  }
}
