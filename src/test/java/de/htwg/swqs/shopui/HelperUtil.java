package de.htwg.swqs.shopui;

import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.model.ShoppingCartItem;
import de.htwg.swqs.catalog.model.Product;
import de.htwg.swqs.order.model.CustomerInfo;
import de.htwg.swqs.shopui.util.ItemRequestWrapper;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class HelperUtil {

  public static Product createDummyProduct(long id) {
    Product p = new Product();
    p.setId(id);
    p.setName("sample product");
    p.setDescription("sample description");
    p.setPriceEuro(new BigDecimal("3.14"));
    return p;
  }

  public static ShoppingCart createDummyShoppingCart() {
    ShoppingCart s = new ShoppingCart();

    ShoppingCartItem itemOne = new ShoppingCartItem();
    itemOne.setQuantity(3);
    itemOne.setProduct(createDummyProduct(1L));

    ShoppingCartItem itemTwo = new ShoppingCartItem();
    itemTwo.setQuantity(12);
    itemTwo.setProduct(createDummyProduct(2L));

    List<ShoppingCartItem> itemList = Arrays.asList(itemOne, itemTwo);
    s.setItemsInShoppingCartAsList(itemList);

    BigDecimal cartTotalSum = new BigDecimal("0.00");
    for (ShoppingCartItem item : itemList) {
      BigDecimal itemTotal = new BigDecimal("0.00");
      itemTotal = item.getProduct().getPriceEuro().multiply(new BigDecimal(item.getQuantity()));
      cartTotalSum = cartTotalSum.add(itemTotal);
    }
    s.setCartTotalSum(cartTotalSum);
    return s;
  }

  public static CustomerInfo createDummyCustomerInfo() {
    CustomerInfo c = new CustomerInfo();
    c.setEmail("max@muster.de");
    c.setCity("Konstanz");
    c.setPostcode("78467");
    c.setStreet("Hauptstraße 3");
    c.setIsoCountryCode("DE");
    c.setFirstname("Max");
    c.setSurname("Muster");
    return c;
  }

  public static ItemRequestWrapper createDummyItemRequestWrapper(long id) {
    ItemRequestWrapper i = new ItemRequestWrapper();
    i.setProductId(id);
    i.setQuantity(3);
    return i;
  }

}
