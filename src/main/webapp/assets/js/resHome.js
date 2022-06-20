const name1 = document.getElementById("resName");
name1.innerHTML = window.localStorage.restaurantName;
const orderDisplay = document.getElementById("orderDisplay1");
var resOrders;
function getOrders() {
  $.ajax({
    method: "GET",
    url: "../../Orders?for=restaurant",
    success: (data) => { 
    orderDisplay.innerHTML = "";
      resOrders = data;
      console.log(data);
      renderOrder(data);
    },
    error: (err) => {
      console.log(err);
    },
  });
}

const totOrders = document.getElementById("totOrders");

var totalPrice = 0;

setTimeout(() => {
  totOrders.innerHTML = resOrders.length;
}, 800);

getOrders();
function renderOrder(data) {
  var orderId = [];
  data.forEach((ele) => {
    if (!orderId.includes(ele.orderId)) {
      orderId.push(ele.orderId);
    }
  });
  var orderDet = [];

  orderId.forEach((ele) => {
    var orderObj = {
      orderId: "",
      orderDate: "",
      totQty: 0,
      totPrice: 0,
      userName: "",
      userAddress: "",
      userEmail: "",
      userPhone: "",
    };
    const data1 = data.filter((ele1) => ele1.orderId === ele);
    orderObj.orderId = ele;
    orderObj.userName = data1[0].userName;
    orderObj.userAddress = data1[0].userAddress;
    orderObj.userEmail = data1[0].userEmail;
    orderObj.userPhone = data1[0].userPhone;

    var date = new Date(data1[0].timeCreated);
    const timestamp =
      date.toLocaleDateString() +
      " " +
      date.toLocaleString([], { hour: "2-digit", minute: "2-digit" });
    orderObj.orderDate = timestamp;
    data1.forEach((ele2) => {
      orderObj.totQty += ele2.quantity;
    });
    data1.forEach((ele2) => {
      orderObj.totPrice += (ele2.foodPrice * ele2.quantity);
      totalPrice += (ele2.foodPrice * ele2.quantity);
    });
    orderDet.push(orderObj);
  }); 
  orderDet.forEach((ele) => {

    var foodNames = [];
    var foodNamesString = "";
    data.forEach((ele1) => {
      if (ele.orderId === ele1.orderId) {
        foodNamesString +=
          "<li>" + ele1.foodName + " - " + ele1.quantity + "</li>";
      }
    });
    const orderTr = `<div class="order" onclick="renderOrderDetails(${ele.orderId})">
    <div class="orderDate">${ele.orderDate}, ${ele.userEmail}, ${ele.userPhone}</div>
    <div class="orderDet"><ul>${foodNamesString}</ul></div>
    <div class="orderQuantity">Total Quantity: ${ele.totQty}</div> 
    <div class="orderPrice">Total Price: ₹ ${ele.totPrice} /-</div>
</div>`;
    orderDisplay.innerHTML += orderTr;
  });
}

function refresh() {
    getOrders();
}
setInterval(() => {
  getOrders();
}, 10000);


//dashboard details

const ratings = document.getElementById("ratings");
const totPrice = document.getElementById("totPrice");
var rating = 0;
function fetchRatings(){
  $.ajax({
    url: '../../Review/getReviewCard',
    method: 'GET',
    success: (data) => {
      rating =parseFloat(data.message);
      console.log(data);
      ratings.innerHTML = rating.toFixed(1);
      totPrice.innerHTML = totalPrice;

    },
    error: (err) => {
      console.log(err);
    }

  });
} 

fetchRatings();


const totFood = document.getElementById("totFood");

function fetchFoods(){
  $.ajax({
    method: 'GET',
    url: '../../Food',
    success: (data) => {
      totFood.innerHTML = data.length;
    },
    error: (err) => {
      console.log(err);
    }
  })
}

fetchFoods();