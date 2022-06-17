var searchKeywords = [];
var food = [];
var restaurant = [];

function fetchAllFoods() {
  $.ajax({
    method: "GET",
    url: "../Food?get=1",
    success: function (data) {
      console.log(data);
      data.forEach((ele) => {
        var temp = {
          id: ele.id,
          name: ele.name.toLowerCase(),
          type: "food",
          resId: ele.restaurantId
        };
        food.push(temp);
      });
    },
    error: function (data) {
      console.log(data);
    },
  });
}

fetchAllFoods();

function fetchAllRestaurants() {
  $.ajax({
    method: "GET",
    url: "../Restaurant",
    success: function (data) {
      data.forEach((ele) => {
        var temp = {
          id: ele.id,
          name: ele.name.toLowerCase(),
          type: "restaurant",
        };
        restaurant.push(temp);
      });
    },
    error: function (data) {
      console.log(data);
    },
  });
}

fetchAllRestaurants();

const searchRes = document.getElementById("searchRes");
const searchBar = document.getElementById("search");
const searchRes1 = document.getElementById("searchRes1");
searchBar.addEventListener("keyup", (event) => {
  searchRes1.style.display="flex";
  const searchKeyword = event.target.value.toLowerCase();
  const searchResult = [];
  restaurant.forEach((res) => {
    const data = res.name.includes(searchKeyword);
    data ? searchResult.push(res) : null;
  });
  food.forEach((food) => {
    const data = food.name.includes(searchKeyword);
    data ? searchResult.push(food) : null;
  });
  renderSearchResult(searchResult);
  if (event.target.value == "") {
    searchRes.innerHTML = "";
  searchRes1.style.display="none";

  }
});

function renderSearchResult(searchResult) {
  searchRes.innerHTML = "";
  searchResult.forEach((res) => {
    var data;
    if (res.type === "restaurant") {
        data = `<li><a href="./restaurantFood.html?id=${res.id}"><div class="imgCon"><img src="../assets/images/restaurant.png" alt=""></div>${res.name}</a></li>`
    }
    else if(res.type === "food"){
        data = `<li><a href="./restaurantFood.html?id=${res.resId}"><div class="imgCon"><img src="../assets/images/food.png" alt=""></div> ${res.name}</a></li>`
    }
    searchRes.innerHTML += data;
  });
}
