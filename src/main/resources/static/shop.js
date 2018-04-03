/* ################################################# */
/*                                                   */
/*                   COOKIE HANDLING                 */
/*        https://stackoverflow.com/a/19189846       */
/*                                                   */
/* ################################################# */

/**
 * get the value of a specific cookie
 *
 * @param sName
 * @returns {string}
 */
document.getCookie = function (sName) {
    sName = sName.toLowerCase();
    var oCrumbles = document.cookie.split(';');
    for (var i = 0; i < oCrumbles.length; i++) {
        var oPair = oCrumbles[i].split('=');
        var sKey = decodeURIComponent(oPair[0].trim().toLowerCase());
        var sValue = oPair.length > 1 ? oPair[1] : '';
        if (sKey === sName)
            return decodeURIComponent(sValue);
    }
    return '';
};

/**
 * set the value of a cookie
 *
 * @param sName
 * @param sValue
 */
document.setCookie = function (sName, sValue) {
    var oDate = new Date();
    oDate.setYear(oDate.getFullYear() + 1);
    var sCookie = encodeURIComponent(sName) + '=' + encodeURIComponent(sValue) + ';expires=' + oDate.toGMTString() + ';path=/';
    document.cookie = sCookie;
};

/**
 * remove a specific cookie (set value to blank)
 *
 * @param sName
 */
document.clearCookie = function (sName) {
    setCookie(sName, '');
};

/* ################################################# */
/*                                                   */
/*                  ADD PRODUCT TO CART              */
/*                                                   */
/* ################################################# */

// eventlistener
document.getElementById("addButton").addEventListener('click', createShoppingCart);

var input = document.getElementById("quantity");
var resultFromRequest;

function createShoppingCart() {
    // var requestUrl = document.location.host + "/carts/create";
    var requestUrl = "/carts/create";
    var xhr = new XMLHttpRequest();
    xhr.open("GET", requestUrl);

    xhr.onreadystatechange = function () {

        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log(xhr.responseText);
                resultFromRequest = xhr.responseText;
                setCartId();
            } else {
                console.error("Status = " + xhr.status);
            }
        } else {
            console.error("ReadyState = " + xhr.readyState)
        }
    };

    xhr.send();
}

function setCartId() {
    console.log("result object", resultFromRequest);
    document.setCookie("cartNumber", resultFromRequest);
    addItemToShoppingCart();

}

function addItemToShoppingCart() {

    // check if cart already exists

    // if not, create

    // get quantity from input field

    // update the quantity in the shopping cart view
    shoppingCartQuantityView = document.getElementById("shopping-cart-quantity");
    console.log("shopping cart quantity svg obj", shoppingCartQuantityView);
    var currentQuantity = Number.valueOf(shoppingCartQuantityView.innerText);
    shoppingCartQuantityView.textContent = 3
}

