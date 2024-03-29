const url = new URL(window.location.href);
const id = url.searchParams.get("id");
console.log(id);
var food = [];
var resDetails;
function getFood(id) {
  $.ajax({
    method: "GET",
    url: "../Restaurant/getRestaurantFood?id=" + id,
    success: function (data) {
      console.log(data);
      food = data.foods;
      resDetails = data.restaurantDetails[0];
      console.log(food);
      console.log("res",resDetails);
      renderResDetails(resDetails);
      renderFood(food);
      //   renderFood();
    },
    error: (err) => {
      console.log(err);
    },
  });
}

getFood(id);

function time24HrTo12hr(time) {
  return new Date("1970-01-01T" + time + "Z").toLocaleTimeString("en-US", {
    timeZone: "UTC",
    hour12: true,
    hour: "numeric",
    minute: "numeric",
  });
}

function renderResDetails(res) {
  var resType = "";
  if (res.resType === "veg") {
    resType = "Veg";
  } else if (res.resType === "non-veg") {
    resType = "Non-Veg";
  } else {
    resType = "Veg and Non-Veg";
  }
  const resDet = document.getElementById("resDet");
  var rating = res.rating;
    rating = rating.toFixed(1); 
    var totRating =
      parseInt(Math.floor(rating)) * 20 +
      (rating - parseInt(Math.floor(rating))) * 20; 
  const data = `<div class="resDetails">
    <div class="img">
        <img src="../assets/images/res.jpg" alt="">
    </div>
    <div class="details">
        <div id="resName">${res.name}</div>
        <div id="address">${res.area}, ${res.town}</div>
        <div id="time">${time24HrTo12hr(res.resStartTime)} - ${time24HrTo12hr(
    res.resEndTime
  )}</div>
        <div id="resType">${resType}+</div>
        ${(res.rating == 0)?"":` <div class="star-rating" title="${totRating}%">
        <div class="back-stars">
            <i class="fa fa-star" aria-hidden="true"></i>
            <i class="fa fa-star" aria-hidden="true"></i>
            <i class="fa fa-star" aria-hidden="true"></i>
            <i class="fa fa-star" aria-hidden="true"></i>
            <i class="fa fa-star" aria-hidden="true"></i>
            
            <div class="front-stars" style="width: ${totRating}%">
                <i class="fa fa-star" aria-hidden="true"></i>
                <i class="fa fa-star" aria-hidden="true"></i>
                <i class="fa fa-star" aria-hidden="true"></i>
                <i class="fa fa-star" aria-hidden="true"></i>
                <i class="fa fa-star" aria-hidden="true"></i>
            </div>
        </div>
    </div>`}
            
    </div>
  </div>`;
  resDet.innerHTML = data;
}

function renderFood(foods) {
  const renderFoods = document.getElementById("renderFoods");
  var i = 0;
  foods.forEach((food) => {
    const data = `<div class="card">
    <div class="imgContainer">
      <img src="../uploads/foodsPic/${
        food.image
      }" alt="Food Pic" srcset="" id="image"> 
    </div>
    <div class="foodDetails">
      <div id="foodName">${food.name}</div>
      <div id="foodDesc">${food.description}</div>
      <div class="rateContainer">
        <div id="rate">₹${food.price}</div>
        <div id="netRate">₹${
          food.price - Math.round((food.price * food.discount) / 100)
        }</div>
      </div>
    </div>
    <div id="offer">${food.discount}% offer</div>
    
    <div id="btnContainer">
    <div id="quantity-${i}" class="quantity">
    <div class="value-button" id="decrease" onclick="decreaseValue(${i})" value="Decrease Value">-</div>
    <input type="number" id="number-${i}" class="number" value="1" />
    <div class="value-button" id="increase" onclick="increaseValue(${i})" value="Increase Value">+</div>
    </div>
    <button class="btn1" id="cart-${food.id}" onclick="addCart(${
      food.id
    },${i})">Add to cart</button>
    </div>
    <button class="btnCancel" id="cancel-${food.id}" onclick="cancel(${
      food.id
    },${i})" hidden> cancel </button>
  </div>`;
    renderFoods.innerHTML += data;
    i++;
  });
}
function increaseValue(i) {
  var value = parseInt(document.getElementById("number-" + i).value, 10);
  value = isNaN(value) ? 1 : value;
  value++;
  document.getElementById("number-" + i).value = value;
}

function decreaseValue(i) {
  var value = parseInt(document.getElementById("number-" + i).value, 10);
  value = isNaN(value) ? 1 : value;
  value < 2 ? (value = 2) : "";
  value--;
  document.getElementById("number-" + i).value = value;
}

const cartNo = document.getElementById("cartNo");
const res_id = id;
const alert2 = document.getElementById("alert");
const alertText = document.getElementById("alertText");
function addCart(id, i) {
  loadingWrapper.hidden = false;

  $.ajax({
    method: "POST",
    url:
      "../Cart?foodId=" +
      id +
      "&quantity=" +
      document.getElementById("number-" + i).value +
      "&restaurantId=" +
      res_id,
    success: (data) => {
      console.log(data);
      if(data.result === "failure"){
        alert2.hidden = false;
        loadingWrapper.hidden = true;
        alertText.innerHTML = "You already have items in cart with different restaurant. Do you want to clear the cart and continue?";
      }
      else{
        setTimeout(() => {
          loadingWrapper.hidden = true;
        }, 500);
        toastsFactory.createToast({
          type: "success",
          icon: "check-circle",
          message: "Added to cart",
          duration: 500000,
        });
        cartNo.innerHTML = data.length > 0 ? data.length : "0";
        
      }
    },
    error: (data) => {
      console.log("err",err.responseText);
      if(data.responseText.result === "failure"){
        alert2.hidden = false;
        loadingWrapper.hidden = true;
        alertText.innerHTML = "You have already have items in cart with different restaurant. Do you want to clear the cart and continue?";
      }
    },
  });
}
const loadingWrapper = document.getElementById("loading-wrapper");

function getCart() {
  loadingWrapper.hidden = false;
  $.ajax({
    method: "GET",
    url: "../Cart",
    success: (data) => {
      console.log(data);
      setTimeout(() => {
        loadingWrapper.hidden = true;
      }, 500);
      cartNo.innerHTML = data.length > 0 ? data.length : "0";

      // renderCard(data);
    },
    error: (err) => {
      console.log(err);
    },
  });
}

getCart();


function fetchAllReviews(){
  $.ajax({
    method:"GET",
    url:"../Review/getReview?id="+id,
    success:(data)=>{
      console.log(data);
      // renderReviews(data);
    },
    error:(err)=>{
      console.log(err);
    }
  })
}

fetchAllReviews();

function renderReview(){
  
}