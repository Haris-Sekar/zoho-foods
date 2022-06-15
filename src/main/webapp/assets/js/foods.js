if (!window.localStorage.getItem("tabAt"))
  window.localStorage.setItem("tabAt", "food");
const addFood = document.getElementById("addFood");
const addCategory = document.getElementById("addCategory");
const allFoods = document.getElementById("allFoods");

const addFoodTab = document.getElementById("addFoodTab");
const addCategoryTab = document.getElementById("addCategoryTab");
const allFoodTab = document.getElementById("allFoodTab");
allFoodTab.style.backgroundColor = "#293241";

const resEmail = document.getElementById("resEmail");
resEmail.value = localStorage.getItem("restaurantEmail");

function displayFood() {
  allFoodTab.style.backgroundColor = "#293241";
  addFoodTab.style.backgroundColor = "#EE6C4D";
  addCategoryTab.style.backgroundColor = "#EE6C4D";
  allFoods.hidden = false;
  addCategory.style.display = "none";
  addFood.hidden = true;
}
function displayAddFood() {
  addFoodTab.style.backgroundColor = "#293241";
  allFoodTab.style.backgroundColor = "#EE6C4D";
  addCategoryTab.style.backgroundColor = "#EE6C4D";

  allFoods.hidden = true;
  addCategory.style.display = "none";
  addFood.hidden = false;
}
function displayAddCategory() {
  addCategoryTab.style.backgroundColor = "#293241";
  addFoodTab.style.backgroundColor = "#EE6C4D";
  allFoodTab.style.backgroundColor = "#EE6C4D";

  allFoods.hidden = true;
  addCategory.style.display = "flex";
  addFood.hidden = true;
}

function addFoodCategory() {
  console.log($("#catForm").serialize());
  $.ajax({
    method: "POST",
    url: "../../Category",
    data: $("#catForm").serialize(),
    success: function (data) {
      window.localStorage.setItem("tabAt", "category");
      console.log(data);
      displayAddCategory();
      fetchAllCategory();
      window.location.reload();
    },
    error: (err) => {
      console.log(err);
    },
  });
}

var categoryList = [];

function fetchAllCategory() {
  $.ajax({
    method: "GET",
    url: "../../Category",
    success: function (data) {
      console.log(data);

      categoryList = data;
    },
    error: (err) => {
      console.log(err);
    },
  });
}
fetchAllCategory();
console.log(categoryList);
setTimeout(() => {
  const table = document.getElementById("categoryTable");
  categoryList.forEach((category) => {
    const row = table.insertRow();
    const cell1 = row.insertCell(0);
    const cell2 = row.insertCell(1);
    const cell3 = row.insertCell(2);
    cell1.innerHTML = category.name;
    cell2.innerHTML =
      "<button onclick=editCat(" +
      category.id +
      ") class='edit-btn'>Edit</button>";
    cell3.innerHTML =
      "<button onclick=deleteCat(" +
      category.id +
      ") class ='delete-btn'>Delete</button>";
  });
}, 1000);

const catName = document.getElementById("catName");
const catSubmit = document.getElementById("catSubmit");
const catCancel = document.getElementById("catCancel");
const catType = document.getElementById("catType");
const catId = document.getElementById("catId");
const text1 = document.getElementById("conDelete");
function editCat(id) {
  var data = categoryList.filter((category) => category.id == id);
  console.log(data[0]);
  catName.value = data[0].name;
  catSubmit.value = "Update";
  catCancel.hidden = false;
  catType.value = "update";
  catId.value = data[0].id;
}

function catCancel1() {
  text1.innerHTML = "";
  catName.value = "";
  catSubmit.value = "Add";
  catCancel.hidden = true;
  catType.value = "insert";
  catId.value = "";
  catSubmit.style.backgroundColor = "#EE6C4D";
  catName.disabled = false;
  catName.style.color = "black";
}

function deleteCat(id) {
  text1.innerHTML = "Are you sure you want to delete this category? if yes all products associated with this category will be deleted";
  text1.style.color = "white";
  var data = categoryList.filter((category) => category.id == id);
  catName.value = data[0].name;
  catSubmit.value = "Delete";
  catSubmit.style.backgroundColor = "red";
  catCancel.hidden = false;
  catType.value = "delete";
  catId.value = data[0].id;
  catName.disabled = true;
  catName.style.color = "red";
}

const tabAt = window.localStorage.getItem("tabAt");
if (tabAt == "food") {
  displayFood();
} else if (tabAt == "addFood") {
  displayAddFood();
} else if (tabAt == "category") {
  displayAddCategory();
}

//add Food scripts

const select = document.getElementById("addFoodCat");
select.innerHTML = "<option value='null'>Select Food Category</option>";
setTimeout(() => {
  categoryList.forEach((category) => {
    const option = document.createElement("option");
    option.value = category.id;
    option.innerHTML = category.name;
    select.appendChild(option);
  });
}, 1000);

function foodAdd() {
  var form = $("#addFoodForm")[0];
  var data = new FormData(form);
  data.append("file", ajaxfile.files[0]);
  $.ajax({
    method: "POST",
    ecnType: "multipart/form-data",
    url: "../../Food",
    data: data,
    cache: false,
    processData: false,
    contentType: false,
    success: function (data) {
      console.log(data);
      setTimeout(() => { 
        window.localStorage.setItem("tabAt", "food");
        window.location.reload();
      }, 10000);
      toastsFactory.createToast({
        message: "Food Added Successfully",
        type: "success",
        duration: 5000,
        icon:'check-circle'
      });
     
    },
    error: (err) => {
      console.log(err);
    },
  });
  console.log($("#addFoodForm").serialize());
}

//fetch all foods
var foodList = [];
function fetchAllFoods() {
  $.ajax({
    method: "GET",
    url: "../../Food",
    success: function (data) {
      console.log(data);
      foodList = data;
      renderFood(data);
    },
    error: (err) => {
      console.log(err);
    },
  });
}

fetchAllFoods();

  function renderFood(foods) {
    const renderFoods = document.getElementById("renderFoods");
    foods.forEach((food) => {
      const data = `<div class="card">
      <div class="imgContainer">
        <img src="../../uploads/foodsPic/${food.image}" alt="Food Pic" srcset="" id="image"> 
      </div>
      <div class="foodDetails">
        <div id="foodName">${food.name}</div>
        <div id="foodDesc">${food.description}</div>
        <div class="rateContainer">
          <div id="time">${food.prepTime} mins</div>
          <div id="dot"><img src="../../assets/images/foodDot.png" /></div>
          <div id="rate">₹${food.price}</div>
          <div id="netRate">₹${food.price - Math.round((food.price * food.discount)/100)}</div>
        </div>
      </div>
      <div id="offer">${food.discount}% offer</div>
      <div id="btnContainer">
        <button class="btn1" onclick="editFood(${food.id})">Edit</button>
        <button class="btn2" onclick="deleteFood(${food.id})" style="background-color:red;">Delete</button>
      </div>
    </div>`;
    renderFoods.innerHTML += data;
    });
  }


function editFood(id) {
  displayAddFood();
  const data = foodList.filter((food) => food.id == id);
  const text = document.getElementById("text");
  console.log(data[0]);
  text.innerHTML = "Edit Food";
  const name = document.getElementById("name");
  const desc = document.getElementById("desc");
  const discount = document.getElementById("discount");
  const price = document.getElementById("price");
  const stock = document.getElementById("stock");
  name.value = data[0].name;
  desc.value = data[0].description;
  discount.value = data[0].discount;
  price.value = data[0].price;
  stock.value = data[0].stock;

}
