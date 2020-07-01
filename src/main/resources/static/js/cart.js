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

function addItem(itemId) {
  var cartId = Cookies.get("cart-id");
  // get the needed infos for the controller call
  var quantity = document.getElementById("quantity-" + itemId).innerHTML;
  var productId = document.getElementById("product-id-" + itemId).innerHTML;

  // send item to server
  var requestUrl = "/carts/addItem";
  var xhr = new XMLHttpRequest();
  xhr.open("POST", requestUrl);

  xhr.onreadystatechange = function () {

    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        //Item bigger than 99
        if( sessionStorage.getItem('amountOfItems') >= 99) {
          sessionStorage.setItem('amountOfItems',
              ((parseInt(sessionStorage.getItem('amountOfItems'))) + ''));
            alert("Amount cannot go over 99");
            console.error("Status = " + xhr.status);
            throw new Error("Amount cannot go over 99!");
        } else {
          // the response is the updated shopping cart
          sessionStorage.setItem('amountOfItems',
              ((parseInt(sessionStorage.getItem('amountOfItems')) + 1) + ''));
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

function substractItem(itemId) {
  var cartId = Cookies.get("cart-id");
  // get the needed infos for the controller call
  var quantity = document.getElementById("quantity-" + itemId).innerHTML;
  var productId = document.getElementById("product-id-" + itemId).innerHTML;

  // send item to server
  var requestUrl = "/carts/substractItem";
  var xhr = new XMLHttpRequest();
  xhr.open("POST", requestUrl);

  xhr.onreadystatechange = function () {

    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        if(parseInt(sessionStorage.getItem('amountOfItems')) === 1) { //changed from 0
          sessionStorage.setItem('amountOfItems',
          ((parseInt(sessionStorage.getItem('amountOfItems'))) + ''));
          alert("Amount cannot go below 1!");
          console.error("Status = " + xhr.status);
          throw new Error("Amount cannot go below 1!");
        } else {

        // the response is the updated shopping cart
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