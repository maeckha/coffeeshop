package de.htwg.swqs.shopui.selenium.pages;

import de.htwg.swqs.shopui.selenium.SeleniumConfig;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class OrderVerifyPage {

  private SeleniumConfig config;

  @FindBy(tagName = "h1")
  private WebElement pageTitle;

  @FindBy(id = "order.customerInfo.email")
  private WebElement customerInfoEmail;

  @FindBy(id = "order.customerInfo.firstname")
  private WebElement customerInfoFirstname;

  @FindBy(id = "order.customerInfo.surname")
  private WebElement customerInfoSurname;

  @FindBy(id = "order.customerInfo.street")
  private WebElement customerInfoStreet;

  @FindBy(id = "order.customerInfo.postcode")
  private WebElement customerInfoPostcode;

  @FindBy(id = "order.customerInfo.city")
  private WebElement customerInfoCity;

  @FindBy(id = "order.customerInfo.isoCountryCode")
  private Select customerInfoIsoCountryCode;

  @FindBy(id = "order.customerInfo.currency")
  private Select customerInfoCurrency;

  @FindBy(id = "submit-order-verify")
  private WebElement submitButton;


  public OrderVerifyPage(SeleniumConfig config) {
    this.config = config;
    PageFactory.initElements(this.config.getDriver(), this);
  }

  public String getPageTitle() {
    return this.pageTitle.getText();
  }

  public void compareTheData() {

    // todo: to be implemented
  }

  public void submitForm() {
    this.submitButton.click();
  }
}


