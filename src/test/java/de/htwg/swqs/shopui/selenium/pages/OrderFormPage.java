package de.htwg.swqs.shopui.selenium.pages;

import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.shopui.HelperUtil;
import de.htwg.swqs.shopui.selenium.SeleniumConfig;
import org.hibernate.id.uuid.Helper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class OrderFormPage {

  private SeleniumConfig config;

  @FindBy(tagName = "h1")
  private WebElement pageTitle;

  @FindBy(id = "customerInfo.email")
  private WebElement customerInfoEmail;

  @FindBy(id = "customerInfo.firstname")
  private WebElement customerInfoFirstname;

  @FindBy(id = "customerInfo.surname")
  private WebElement customerInfoSurname;

  @FindBy(id = "customerInfo.street")
  private WebElement customerInfoStreet;

  @FindBy(id = "customerInfo.postcode")
  private WebElement customerInfoPostcode;

  @FindBy(id = "customerInfo.city")
  private WebElement customerInfoCity;

  @FindBy(id = "customerInfo.isoCountryCode")
  private WebElement customerInfoIsoCountryCode;

  @FindBy(id = "currency")
  private WebElement customerInfoCurrency;

  @FindBy(id = "submit-order-form")
  private WebElement submitButton;


  public OrderFormPage(SeleniumConfig config) {
    this.config = config;
    PageFactory.initElements(this.config.getDriver(), this);
  }

  public String getPageTitle() {
    return this.pageTitle.getText();
  }

  public void fillInFormWithDummyData() {
    CustomerInfo dummyCustomerInfo = HelperUtil.createDummyCustomerInfo();
    this.customerInfoEmail.sendKeys(dummyCustomerInfo.getEmail());
    this.customerInfoFirstname.sendKeys(dummyCustomerInfo.getFirstname());
    this.customerInfoSurname.sendKeys(dummyCustomerInfo.getSurname());
    this.customerInfoStreet.sendKeys(dummyCustomerInfo.getStreet());
    this.customerInfoCity.sendKeys(dummyCustomerInfo.getCity());
    this.customerInfoPostcode.sendKeys(dummyCustomerInfo.getPostcode());

    Select countryDropdown= new Select(this.customerInfoIsoCountryCode);
    countryDropdown.selectByValue(dummyCustomerInfo.getIsoCountryCode());
    Select currencyDropdown= new Select(this.customerInfoCurrency);
    currencyDropdown.selectByValue("EUR");
  }

  public void submitForm() {
    this.submitButton.click();
  }
}
