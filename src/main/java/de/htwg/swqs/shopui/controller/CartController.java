package de.htwg.swqs.shopui.controller;

import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.model.ShoppingCartItem;
import de.htwg.swqs.cart.service.CartService;
import de.htwg.swqs.shopui.util.ItemRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("carts/")
public class CartController {

    private CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;

    }

    @GetMapping(value = "/create")
    public long createNewCartAndReturnId() {

        ShoppingCart newCart = this.cartService.createNewShoppingCart();
        return  newCart.getId();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ShoppingCart getCartById(@PathVariable long id) {
        // return the full shopping cart with sum and all products
        return this.cartService.getShoppingCart(id);
    }

    @GetMapping(value = "/{id}/clear")
    public ShoppingCart removeCartById(@PathVariable long id) {

        return this.cartService.clearShoppingCart(id);
    }

    @PostMapping(value = "/{cartId}/add-item", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ShoppingCart addItemToCart(@PathVariable int cartId, @RequestBody ItemRequestWrapper itemRequestWrapper) {
        System.err.println("add item called");
        return this.cartService.addItemToCart((long) cartId, itemRequestWrapper.getProductId(), itemRequestWrapper.getQuantity());
    }

    @PostMapping(value = "/{cartId}/remove-item", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ShoppingCart removeItemFromCart(@PathVariable int cartId, @RequestBody ItemRequestWrapper itemRequestWrapper) {
        System.err.println("remove item called");
        System.err.println("cart id = " + cartId + " | product id = " + itemRequestWrapper.getProductId() + " | quantity = " + itemRequestWrapper.getQuantity());
        return this.cartService.removeItemFromCart((long) cartId, itemRequestWrapper.getProductId(), itemRequestWrapper.getQuantity());
    }

}
