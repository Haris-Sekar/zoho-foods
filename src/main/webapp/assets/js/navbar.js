const navbarCon = document.getElementById("navbarCon");

$.ajax({
  method: "GET",
  url: "../UserValidation",
  error: (error) => {
    console.log("err", error);
  },
  success: (data) => {
    renderNavbar(data);
  },
});

function renderNavbar(data) {
  if (data.result === "failure") {
    navbarCon.innerHTML = `<div class="logo">
    <a href="">
        <img src="../assets/images/logo.png" alt="">
    </a>
</div>
<div class="navContent">
    <a href="./restaurant/login.html" class="navConText">Restaurant Login</a>
    <a href="./login.html"><button class="btnnavbar">Login</button></a>
    <a href="./signup.html"><button class="btnnavbar">Signup</button></a>
</div>`
  } else {
    navbarCon.innerHTML = `<div class="logo">
    <a href="../index.html">
        <img src="../assets/images/logo.png" alt="">
    </a>
</div>
<div class="navContent">
    <a href="./restaurant.html" class="navConText">Restaurants</a>
    <a href="./orders.html" class="navConText">Orders</a>
    <a href="./cart.html" class="navConText">
        <div class="navCart">
            <div class="navCart-icon">
                <img src="../assets/images/cart.png" alt="">
            </div>
            <div class="navCart-badge"><span id="cartNo">0</span></div>
        </div>
    </a>
    <div class="profilePic" id="profilePic">
        <div class="imgCon" id="imgConProfile"></div>
        <div class="dropDown" id="dropDown">
            <a href="./account.html" class="navConText1">Account</a>
            <div class="navConText1" onclick="logout()">Logout</div>
        </div>
    </div>
</div>` 
    renderProfileImg(data.profilePic);
  }
}

const imgCon = document.getElementById("profilePic");
const dropDown = document.getElementById("dropDown");
imgCon.addEventListener("mouseenter", function () {
  dropDown.style.display = "block";
});
imgCon.addEventListener("mouseleave", function () {
  dropDown.style.display = "none";
});
function renderProfileImg(data) {
  const profileImg = document.getElementById("imgConProfile");
  profileImg.innerHTML = `<img src="../uploads/profilePic/${data}" alt="" id="profileImg">`;
}
