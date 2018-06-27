/**
 * If a cookie for the cart id exists we make the link for the shopping cart
 * accessible and set the correct amount of items in cart on the icon on the
 * right side.
 *
 */
window.onload = function () {
  if (Cookies.get("cart-id") !== undefined) {
    document.getElementById("nav-cart").style.pointerEvents = "all";
    document.getElementById("nav-cart").style.opacity = 1;
    document.getElementById("shopping-cart-wrapper").style.opacity = 1;
    document.getElementById(
        "shopping-cart-quantity").textContent = sessionStorage.getItem(
        'amountOfItems');

  }
};

/**
 * Used for the 'cancel' buttons in the order process. Remove the cookie and
 * clear the session storage. Redirect the user to the homepage.
 *
 */
function cancelOrder() {
  sessionStorage.removeItem('amountOfItems');
  Cookies.remove('cart-id');
  window.location.href = '..';
}
