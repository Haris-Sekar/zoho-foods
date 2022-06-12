var restaurants = [];
const loadingWrapper = document.getElementById("loading-wrapper");

function fetchRestaurant() {
  loadingWrapper.hidden = false;
  // console.log(window.location.search);
  var url;
  if (
    window.location.href ===
    "http://localhost:8080/Zoho_Food/" || window.location.href === "http://localhost:8080/Zoho_Food/index.html"
  ) {
    url = "Restaurant";
  } else {
    url = "../Restaurant";
  }
  $.ajax({
    method: "POST",
    url: url,
    success: (data) => {
      console.log(data);
      restaurants = data;
      setTimeout(() => {
        loadingWrapper.hidden = true;
      }, 500);

      renderRestaurant();
    },
    error: (err) => {
      console.log(err);
    },
  });
}

fetchRestaurant();
const cartNo = document.getElementById("cartNo");
function getCartDet() {
  $.ajax({
    method: "GET",
    url: "../Cart",
    success: (data) => {
      console.log(data);
      cartNo.innerHTML = data.length;
    },
    error: (err) => {
      console.log(err);
    },
  });
}

getCartDet();
const resCardId = document.getElementById("resCardId");
function time24HrTo12hr(time) {
  return new Date("1970-01-01T" + time + "Z").toLocaleTimeString("en-US", {
    timeZone: "UTC",
    hour12: true,
    hour: "numeric",
    minute: "numeric",
  });
}

function renderRestaurant() {
  restaurants.forEach((res) => {
    var resType = "";
    if (res.resType === "veg") {
      resType = "Veg";
    } else if (res.resType === "non-veg") {
      resType = "Non-Veg";
    } else {
      resType = "Veg and Non-Veg";
    }
    const status = "";
    const today = new Date();
    console.log(
      today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds()
    );
    // if(res.resStartTime)
    var imgUrl;
    if (
      window.location.href ===
      "http://localhost:8080/Zoho_Food/" || window.location.href === "http://localhost:8080/Zoho_Food/index.html"
    ) {
      imgUrl = "./assets/images/res.jpg";
    } else {
      imgUrl = "../assets/images/res.jpg";
    }
    const card = `
        <a href ="./restaurantFood.html?id=${res.id}" class="res">
        <div class="resCard">
        <div class="resDetails">
            <div class="img">
                <img src="${imgUrl}" alt="">
            </div>
            <div class="details">
                <div id="resName">${res.name}</div>
                <div id="address">${res.area}, ${res.town}, ${res.state}</div>
                <div id="time">${time24HrTo12hr(
                  res.resStartTime
                )} - ${time24HrTo12hr(res.resEndTime)}</div>
                <div id="resType">${resType}</div>
            </div>
        </div>  
    
    </div>
    </a>`;
    resCardId.innerHTML += card;
  });
}
