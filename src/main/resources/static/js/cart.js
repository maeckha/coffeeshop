/**
 * This class is used in the shopping cart overview.
 * Handles the 'remove item' and 'go to order' action.
 *
 * @author Mirko Bay
 *
 */


document.getElementById("order-button").addEventListener('click',
    redirectToOrderPage);

function redirectToOrderPage() {
  window.location.href = "../order";
}

function removeItem(itemId) {
  var cartId = Cookies.get("cart-id");
  console.log("itemId", itemId);
  // get the needed infos for the controller call
  console.log("quantity-" + itemId);
  var quantity = document.getElementById("quantity-" + itemId).innerHTML;
  var productId = document.getElementById("product-id-" + itemId).innerHTML;

  // send item to server
  var requestUrl = "/carts/remove";
  console.log(requestUrl);
  var xhr = new XMLHttpRequest();
  xhr.open("POST", requestUrl);

  xhr.onreadystatechange = function () {

    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        // the response is the updated shopping cart
        console.log("xhr response", xhr.responseText);
        location.reload();
      } else {
        console.error("Status = " + xhr.status);
      }
    }
  };

  xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
  xhr.send(JSON.stringify({productId: productId, quantity: quantity}));
}