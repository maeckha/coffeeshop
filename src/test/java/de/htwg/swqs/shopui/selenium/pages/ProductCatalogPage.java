package de.htwg.swqs.shopui.selenium.pages;

import de.htwg.swqs.shopui.selenium.SeleniumConfig;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductCatalogPage {

  private SeleniumConfig config;

  @FindBy(tagName = "article")
  private List<WebElement> productElements;
  @FindBy(tagName = "h1")
  private WebElement title;

  public ProductCatalogPage(SeleniumConfig config) {
    this.config = config;
    PageFactory.initElements(this.config.getDriver(), this);
  }

  public void navigate() {
    this.config.navigateTo("/products");
  }

  public List<WebElement> getProductElements() {
    return this.productElements;
  }

  public String getPageTitle() {
    return this.title.getText();
  }

}
