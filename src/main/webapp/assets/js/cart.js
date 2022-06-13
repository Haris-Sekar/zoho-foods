const loadingWrapper = document.getElementById("loading-wrapper");
const orderSuccess = document.getElementById("orderSuccess");
const emptyCart = document.getElementById("emptyCart");
const cart = document.getElementById("cart");
function getCartDet() {
  loadingWrapper.hidden = false;
  $.ajax({
    method: "GET",
    url: "../Cart?",
    success: (data) => {
      console.log(data);
      setTimeout(() => {
        loadingWrapper.hidden = true;
      }, 500);
      cartNo.innerHTML = data.length > 0 ? data.length : "0";
      if(data.length <= 0){
        emptyCart.hidden = false;
        cart.style.display = "none";
      }
      else{
        emptyCart.hidden = true;
        cart.style.display = "flex";
      }
      renderCartData(data);
      setTotalBill(data);
    },
    error: (err) => {
      console.log(err);
    },
  });
}

getCartDet();

var totDisAmt = 0;
function renderCartData(data) {
  const cartItems = document.getElementById("cartItems");
  data.forEach((ele) => {
    totDisAmt += Math.round((ele.price * ele.discount) / 100) * ele.quantity;
    const foodType = ele.foodType === "veg" ? "veg.png" : "nv.png";
    const cartData = `<div class="card">
        <div class="foodDetContainer">
            <div class="imgDelete" onclick="deleteCart(${
              ele.id
            })"><img src="../assets/images/cancel.png"></div>
            <div class="imgCon"><img src="../uploads/foodsPic/${
              ele.image
            }" alt=""></div>
            <div class="foodDet">
              <img src="../assets/images/${foodType}" id="foodTypeImg">
              <div id="foodName">${
                ele.name
              }<span class="badge badge--success badge--small" style="margin: 0 1% 0 0">-${
      ele.discount
    }%</span></div>  
            </div>
        </div>
        <div class="rateContainer">
            <div class="rates">
                <div id="rate">₹ ${ele.price}/-</div>
                <div id="finRate">₹ ${
                  ele.price - Math.round((ele.price * ele.discount) / 100)
                }</div>
                <div id="mul">X</div>
                <div id="quantity">${ele.quantity}</div>
                </div>
                <div id="netRate">₹ ${
                  (ele.price - Math.round((ele.price * ele.discount) / 100)) *
                  ele.quantity
                }/- </div>
        </div>
    </div>`;
    cartItems.innerHTML += cartData;
  });
}

function deleteCart(id) {
  loadingWrapper.hidden = false;
  $.ajax({
    method: "GET",
    url: `../DeleteCart?id=${id}`,
    success: (data) => {
      console.log(data);
      toastsFactory.createToast({
        type: "success",
        icon: "check-circle",
        message: "Deleted from cart",
        duration: 2000,
      });
      setTimeout(() => {
        window.location.reload();
      }, 2000);
    },
    error: (err) => {
      console.log(err);
    },
  });
}

var userId;

function setTotalBill(data) {
  const billResName = document.getElementById("billResName");
  const billRes = document.getElementById("billResAdd");
  const billResType = document.getElementById("billResType");
  const totrate = document.getElementById("totrate");
  const deliveryFee = document.getElementById("deliveryFee");
  const gst = document.getElementById("gst");
  const Grate = document.getElementById("Grate");
  var totSave = document.getElementById("totSave1");
  var totalRate = 0;
  var deliveryFeeAmt = 50;
  var gstAmt = 0;
  var GrateAmt = 0;
  totSave.innerHTML = "₹ " + totDisAmt + "/-";

  data.forEach((ele) => {
    userId = ele.userId;
    var type1;
    if (ele.type === "veg") {
      type1 = "Veg";
    } else if (ele.type === "both") {
      type1 = "Veg & Non-Veg";
    } else {
      type1 = "Non-Veg";
    }

    billResAdd.innerHTML = ele.area + ", " + ele.town;
    billResName.innerHTML = ele.res_name;
    billResType.innerHTML = type1;
    totalRate +=
      (ele.price - Math.round((ele.price * ele.discount) / 100)) * ele.quantity;
  });

  gstAmt = Math.round((totalRate * 5) / 100);
  GrateAmt = totalRate + gstAmt + deliveryFeeAmt;
  totrate.innerHTML = "₹ " + totalRate + "/-";
  deliveryFee.innerHTML = "₹ " + deliveryFeeAmt + "/-";
  gst.innerHTML = "₹ " + gstAmt + "/-";
  Grate.innerHTML = "₹ " + GrateAmt + "/-";
}

const placeOrderBtn = document.getElementById("placeOrder");

placeOrderBtn.addEventListener("click", () => {
  loadingWrapper.hidden = false;
  $.ajax({
    method: "GET",
    url: "../PlaceOrder?userId=" + userId,
    success: (data) => {
      console.log(data);
      loadingWrapper.hidden = true;

      toastsFactory.createToast({
        type: "success",
        icon: "check-circle",
        message: "Order Placed Successfully",
        duration: 2000,
      });

      orderSuccess.style.display = "flex";
    },
    error: (err) => {
      console.log(err);
    },
  });
  console.log(userId);
});
