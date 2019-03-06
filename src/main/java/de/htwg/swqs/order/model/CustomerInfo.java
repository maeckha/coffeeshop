package de.htwg.swqs.order.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class CustomerInfo implements Comparable<CustomerInfo> {

  @Id
  @GeneratedValue
  private long id;
  private String email;
  private String surname;
  private String firstname;
  private String street;
  private String city;
  private String postcode;
  private String isoCountryCode;


  public CustomerInfo() {

  }

  /**
   * Creates a new customer info.
   *
   * @param id id of the new customer info
   * @param email email of the address
   * @param surname the surname
   * @param firstname the firstname
   * @param street the street
   * @param city the city
   * @param postcode the postcode
   */
  public CustomerInfo(long id, String email, String surname, String firstname, String street,
      String city,
      String postcode, String isoCountryCode) {
    this.id = id;
    this.email = email;
    this.surname = surname;
    this.firstname = firstname;
    this.street = street;
    this.city = city;
    this.postcode = postcode;
    this.isoCountryCode = isoCountryCode;
  }

  public String getEmail() {
    return this.email;
  }

  public long getId() {
    return this.id;
  }

  public String getSurname() {
    return this.surname;
  }

  public String getFirstname() {
    return this.firstname;
  }

  public String getStreet() {
    return this.street;
  }

  public String getCity() {
    return this.city;
  }

  public String getPostcode() {
    return this.postcode;
  }

  public String getIsoCountryCode() {
    return isoCountryCode;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public void setIsoCountryCode(String isoCountryCode) {
    this.isoCountryCode = isoCountryCode;
  }

  public String getFullName() {
    return this.firstname + " " + this.surname;
  }

  public String getFullAddress() {
    return this.street + " " + this.city + " " + this.postcode;
  }

  @Override
  public int compareTo(CustomerInfo customerInfo) {
    if (
        customerInfo.email.compareTo(this.email) == 0
            && customerInfo.firstname.compareTo(this.firstname) == 0
            && customerInfo.surname.compareTo(this.surname) == 0
            && customerInfo.street.compareTo(this.street) == 0
            && customerInfo.city.compareTo(this.city) == 0) {
      return 0;
    } else {
      return -1;
    }
  }

  @Override
  public String toString() {
    return "CustomerInfo{"
        + "email=" + email
        + ", surname='" + surname + '\''
        + ", firstname='" + firstname + '\''
        + ", street='" + street + '\''
        + ", city='" + city + '\''
        + ", postcode='" + postcode + '\''
        + ", isoCountryCode='" + isoCountryCode + '\''
        + '}';
  }
}