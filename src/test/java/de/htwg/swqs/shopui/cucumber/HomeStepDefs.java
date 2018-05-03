package de.htwg.swqs.shopui.cucumber;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.htwg.swqs.shopui.ShopUiApplication;
import de.htwg.swqs.shopui.controller.HomeController;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ShopUiApplication.class})
@WebAppConfiguration
public class HomeStepDefs {

  private MockMvc mvc;

  private static int responseStatus;

  @When("^the client calls /$")
  public void the_client_calls() throws Throwable {
    HomeController homeController = new HomeController();
    this.mvc = MockMvcBuilders.standaloneSetup(homeController).build();

    MvcResult result = this.mvc.perform(get("/"))
        .andExpect(view().name("index")).andReturn();
    MockHttpServletResponse response = result.getResponse();
    responseStatus = response.getStatus();
  }

  @Then("^the client receives status code of (\\d+)$")
  public void the_client_receives_status_code_of(int statusCode) throws Throwable {
    assertEquals("status code is incorrect : " + responseStatus,
        responseStatus,
        statusCode);
  }
}
