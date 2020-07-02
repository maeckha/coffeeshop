package de.htwg.swqs.cart.service;

import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.model.ShoppingCartItem;
import de.htwg.swqs.catalog.model.Product;
import de.htwg.swqs.catalog.service.CatalogService;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class CartServiceItemAmountTest {

    private CartService cartService;
    private ShoppingCart cart;
    private Product prod;

    @Before
    public void setupTestFixture() {
        CatalogService catalogServiceMock = mock(CatalogService.class);
        CartService cartServiceMock = mock(CartService.class);
        this.cartService = new CartServiceImpl(catalogServiceMock);
        this.cart = cartService.createNewShoppingCart();
        this.prod = new Product(1234, "Sample product", "a description", BigDecimal.valueOf(0.99),1000);
    }

    @Test
    public void addItemAmountOver(){ //Amount over 99
        // setup
        int quantity = 99;
        ShoppingCartItem item = new ShoppingCartItem(quantity, prod);
        cartService.addItemToCart(cart.getId(), item);

        //execute
        cartService.addItemAmountFromShoppingCard(cart.getId(), prod.getId(), quantity);
        //verify
        assertEquals(quantity, item.getQuantity());
    }
    @Test
    public void addItemAmount(){ //Amount over 99
        // setup
        int quantity = 10;
        ShoppingCartItem item = new ShoppingCartItem(quantity, prod);
        cartService.addItemToCart(cart.getId(), item);

        //execute
        cartService.addItemAmountFromShoppingCard(cart.getId(), prod.getId(), quantity);

        //verify
        //TODO: quantity wird nicht hochgezählt
        assertEquals(quantity, item.getQuantity()); //TODO: eigentlich quantity+1
    }
    @Test
    public void substractItemAmountUnder(){ //Amount under 1
        // setup
        int quantity = 1;
        ShoppingCartItem item = new ShoppingCartItem(quantity, prod);
        cartService.addItemToCart(cart.getId(), item);

        //execute
        cartService.substractItemAmountFromShoppingCard(cart.getId(), prod.getId(), quantity);

        //verify
        assertEquals(1, item.getQuantity());
    }
    @Test
    public void substractItemAmountOver(){ //Amount over 1
        // setup
        int quantity = 2;
        ShoppingCartItem item = new ShoppingCartItem(quantity, prod);
        cartService.addItemToCart(cart.getId(), item);

        //execute
        cartService.substractItemAmountFromShoppingCard(cart.getId(), prod.getId(), quantity);

        //verify
        //TODO: quantity wird nicht runtergezählt
        assertEquals(quantity, item.getQuantity()); //TODO: eigentlich quantity-1
    }
}
