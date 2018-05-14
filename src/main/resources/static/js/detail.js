/**
 * This class is used in the detail view of the products.
 * Handles the 'add to cart' action and creates a new cart when needed.
 *
 * @author Mirko Bay
 *
 */

/**
 * Start point when the user clicks the 'add' button.
 */
function checkIfCartExist() {

  // first check if the input is valid
  if (!document.getElementById("quantity").checkValidity()) {
    // input is not valid
    return false;
  }

  // the value is valid, we can go on

  // check if cart already exists
  if (Cookies.get('cart-id') === undefined) {
    // cart does not exist -> create!
    createShoppingCartAndSetId();
  } else {
    addItemToCart();
  }
}

/**
 * If the user got no shopping cart, we must retrieve a new id from the
 * backend and set this as cookie.
 */
function createShoppingCartAndSetId() {
  // var requestUrl = document.location.host + "/carts/create";
  var requestUrl = "/carts/create";
  var xhr = new XMLHttpRequest();
  xhr.open("GET", requestUrl);

  xhr.onreadystatechange = function () {

    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        Cookies.set('cart-id', xhr.responseText);
        addItemToCart();

      } else {
        alert("Error while creating a new shopping cart!");
        console.error("Status = " + xhr.status);
      }
    }
  };

  xhr.send();
  sessionStorage.setItem('amountOfItems', 0);

}

/**
 * Get the infos of the product and the quantity and
 * send this to backend (addItemToCart(...)
 */
function addItemToCart() {
  // get the needed infos for the controller call
  var cartId = Cookies.get('cart-id');
  var quantity = document.getElementById("quantity").value;
  var productId = document.getElementById("product-id").innerHTML;

  // send item to server
  // var requestUrl = document.location.host + "/carts/create";
  var requestUrl = "/carts/add";
  var xhr = new XMLHttpRequest();
  xhr.open("POST", requestUrl);

  xhr.onreadystatechange = function () {

    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        // the response is the updated shopping cart
        console.log("xhr response", xhr.responseText);
        updateAndShowCartPreview(quantity);

      } else {
        alert("Error while adding the item!");
        console.error("Status = " + xhr.status);
      }
    }
  };

  xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
  xhr.send(JSON.stringify({productId: productId, quantity: quantity}));
}

/**
 * Update the amount of items in the shopping cart icon on the right side.
 *
 * @param addedQuantity The quantity of items the user added
 */
function updateAndShowCartPreview(addedQuantity) {

  document.getElementById("nav-cart").style.pointerEvents = "all";
  document.getElementById("nav-cart").style.opacity = 1;

  // calculate new quantity of items
  var currentQuantity = sessionStorage.getItem('amountOfItems');
  var newQuantity = parseInt(currentQuantity) + parseInt(addedQuantity);

  // update the amount of items in the shopping cart icon
  // and the global variable in the session storage
  document.getElementById("shopping-cart-quantity").textContent = newQuantity;
  sessionStorage.setItem('amountOfItems', newQuantity);

  // show the cart icon
  document.getElementById("shopping-cart-wrapper").style.opacity = 1;
}
