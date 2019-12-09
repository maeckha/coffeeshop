/**
 * This class is used in the shopping cart overview.
 * Handles the 'remove item' and 'go to order' action.
 *
 * @author Mirko Bay
 *
 */


document.getElementById("order-button").addEventListener('click',
    function () {
      window.location.href = "../order";
    });


function removeItem(itemId) {
  var cartId = Cookies.get("cart-id");
  // get the needed infos for the controller call
  var quantity = document.getElementById("quantity-" + itemId).innerHTML;
  var productId = document.getElementById("product-id-" + itemId).innerHTML;

  // send item to server
  var requestUrl = "/carts/remove";
  var xhr = new XMLHttpRequest();
  xhr.open("POST", requestUrl);

  xhr.onreadystatechange = function () {

    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        // the response is the updated shopping cart
        sessionStorage.setItem('amountOfItems',
            (sessionStorage.getItem('amountOfItems') - quantity)
        );
        location.reload();
      } else {
        alert("Error while removing item!");
        console.error("Status = " + xhr.status);
      }
    }
  };

  xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
  xhr.send(JSON.stringify({productId: productId, quantity: quantity}));
}

function changeItem(itemId, isAdded) {
  var cartId = Cookies.get("cart-id");
  // get the needed infos for the controller call
  var quantity = document.getElementById("quantity-" + itemId).innerHTML;
  var productId = document.getElementById("product-id-" + itemId).innerHTML;

  // send item to server
  var requestUrl = "/carts/change";
  var xhr = new XMLHttpRequest();
  xhr.open("POST", requestUrl);

  xhr.onreadystatechange = function () {

    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        // the response is the updated shopping cart
        if(isAdded) {
          sessionStorage.setItem('amountOfItems',
              ((parseInt(sessionStorage.getItem('amountOfItems')) + 1) + '')
          );
        } else {
          sessionStorage.setItem('amountOfItems',
              ((parseInt(sessionStorage.getItem('amountOfItems')) - 1) + '')
          );
        }

        location.reload();
      } else {
        alert("Error while removing item!");
        console.error("Status = " + xhr.status);
      }
    }
  };

  xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
  xhr.send(JSON.stringify({productId: productId, quantity: quantity}));
}