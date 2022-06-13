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
      orderObj.totPrice += ele2.foodPrice;
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
    const orderTr = `<div class="order" onclick="renderOrderDetails(${ele.orderId})" id="sepOrder-${ele.orderId}" >
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

function renderOrderDetails(id) {
  document.getElementById("orderDisplay").innerHTML = ` <tr>
  <th>Order Date</th>
  <th>Name</th>
  <th>Quantity</th> 
</tr>`;
  var orderDet = resOrders.filter((ele) => ele.orderId === id);
  const customerName = document.getElementById("customerName");
  const customerAddress = document.getElementById("customerAddress");
  const customerEmail = document.getElementById("customerEmail");
  const customerPhone = document.getElementById("customerPhone");
  customerName.innerHTML = orderDet[0].userName;
  customerAddress.innerHTML = orderDet[0].userAddress;
  customerEmail.innerHTML = orderDet[0].userEmail;
  customerPhone.innerHTML = orderDet[0].userPhone;
  var date = new Date(orderDet[0].timeCreated);
  const timestamp =
    date.toLocaleDateString() +
    " " +
    date.toLocaleString([], { hour: "2-digit", minute: "2-digit" });
  orderDet.forEach((ele) => {
    const orderTr = `<tr>
    <td>${timestamp}</td>
    <td>${ele.foodName}</td>
    <td>${ele.quantity}</td> 
    </tr>`;
    document.getElementById("orderDisplay").innerHTML += orderTr;
  });

  console.log(orderDet);
}
