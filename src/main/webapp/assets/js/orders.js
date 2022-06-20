var orderData = [];
const cartNo = document.getElementById("cartNo");
function getCartDet() {
  $.ajax({
    method: "GET",
    url: "../Cart",
    success: (data) => {
      console.log(data);
      cartNo.innerHTML = data.length > 0 ? data.length : "0";
    },
    error: (err) => {
      console.log(err);
    },
  });
}

getCartDet();

function getOrders() {
  $.ajax({
    method: "GET",
    url: "../Orders",
    success: (data) => {
      console.log(data);
      renderOrder(data);
      orderData = data;
    },
    error: (err) => {
      console.log(err);
    },
  });
}

getOrders();

const orderDisplay = document.getElementById("orderDisplay");
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
      resName: "",
      totQty: 0,
      totPrice: 0,
    };
    const data1 = data.filter((ele1) => ele1.orderId === ele);
    orderObj.orderId = ele;
    var date = new Date(data1[0].timeCreated);
    const timestamp =
      date.toLocaleDateString() +
      " " +
      date.toLocaleString([], { hour: "2-digit", minute: "2-digit" });
    orderObj.orderDate = timestamp;
    orderObj.resName = data1[0].resName;
    data1.forEach((ele2) => {
      orderObj.totQty += ele2.quantity;
    });
    data1.forEach((ele2) => {
      orderObj.totPrice += (ele2.foodPrice * ele2.quantity);
    });
    orderDet.push(orderObj);
  });
  console.log(orderDet);
  orderDet.forEach((ele) => {
    const orderTr = `<tr id="order-${ele.orderId}" onclick="specificOrder(${ele.orderId})"> 
    <td>${ele.orderDate}</td>
    <td>${ele.resName}</td>
    <td>${ele.totQty}</td>
    <td>₹ ${ele.totPrice} /-</td>
  </tr>`;
    orderDisplay.innerHTML += orderTr;
  });
}

function specificOrder(id) {
  const orderContainer = document.getElementById("orderContainer");
  const orderDetailsCon = document.getElementById("orderDetailsCon");
  orderContainer.style.width = "60%";

  orderDetailsCon.style.display = "block";
  const orderDetailsDisplay = document.getElementById("orderDetailsDisplay");
  // console.log(orderData);
  const data = orderData.filter((ele) => ele.orderId === id);
  console.log(data);
  var date = new Date(data[0].timeCreated);
    const timestamp =
      date.toLocaleDateString() +
      " " +
      date.toLocaleString([], { hour: "2-digit", minute: "2-digit" });
  orderDetailsDisplay.innerHTML = `<tr style="background-color: #239241"><th>${data[0].resName}</th><th>${timestamp.split(" ")[0]}</th><th>${timestamp.split(" ")[1]} ${timestamp.split(" ")[2]}</th><th></th><th></th></tr>`;
  orderDetailsDisplay.innerHTML += "<tr style='background-color: #EE6C4D'> <th>Name</th> <th>Food Type</th><th>Food Price</th> <th>Quantity</th> <th>Net Price</th> </tr>";
  data.forEach((ele) => {
    var foodType = "";
    if (ele.foodType === "veg") {
      foodType = "Veg";
    } else if (ele.foodType === "nveg") {
      foodType = "Non-Veg";
    }
    const orderDet = `<tr>
        <td>${ele.foodName}</td>
        <td>${foodType}</td>
        <td>${ele.foodPrice}</td>
        <td>${ele.quantity}</td>
        <td>₹ ${ele.foodPrice * ele.quantity}/-</td>
    </tr>`;
    orderDetailsDisplay.innerHTML += orderDet;
  });
}
