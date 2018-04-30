// check if a shopping cart exists (for showing / hiding the link)
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

function cancelOrder() {
  sessionStorage.removeItem('amountOfItems');
  Cookies.remove('cart-id');
  window.location.href = '..';
}
