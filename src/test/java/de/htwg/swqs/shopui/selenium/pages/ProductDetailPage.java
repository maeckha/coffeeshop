package de.htwg.swqs.shopui.selenium.pages;

import de.htwg.swqs.shopui.selenium.SeleniumConfig;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductDetailPage {

  private SeleniumConfig config;

  @FindBy(tagName = "article")
  private List<WebElement> productElements;
  @FindBy(tagName = "h1")
  private WebElement productName;
  @FindBy(id = "quantity")
  private WebElement productQuantity;
  @FindBy(id = "addButton")
  private WebElement addButton;

  private long productId;

  public ProductDetailPage(SeleniumConfig config, long productId) {
    this.config = config;
    this.productId = productId;
    PageFactory.initElements(this.config.getDriver(), this);
  }

  public void navigate() {
    this.config.navigateTo("/products/" + this.productId);
  }

  public List<WebElement> getProductElements() {
    return this.productElements;
  }

  public String getProductName() {
    return this.productName.getText();
  }

  public void addItemToCart(int amount) {
    this.productQuantity.clear();
    this.productQuantity.sendKeys(String.valueOf(amount));
    this.addButton.click();
  }
}
