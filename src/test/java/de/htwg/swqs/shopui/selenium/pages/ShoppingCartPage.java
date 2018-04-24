package de.htwg.swqs.shopui.selenium.pages;

import de.htwg.swqs.shopui.selenium.SeleniumConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ShoppingCartPage {

  private SeleniumConfig config;

  @FindBy(tagName = "table")
  private WebElement itemTable;
  @FindBy(tagName = "h1")
  private WebElement title;

  public ShoppingCartPage(SeleniumConfig config) {
    this.config = config;
    PageFactory.initElements(this.config.getDriver(), this);
  }

  public void navigate() {
    this.config.navigateTo("/show-cart");
  }

  public WebElement getItemTable() {
    return this.itemTable;
  }

  public String getPageTitle() {
    return this.title.getText();
  }

  public void clickOrderButton() {
    this.config.getDriver().findElement(By.id("order-button")).click();
  }
}
