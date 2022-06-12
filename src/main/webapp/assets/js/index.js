const email = window.localStorage.getItem('email');
if(email != undefined || email != null){
  console.log("hi");
  window.location.replace('./template/home.html');
}

const resEmail = window.localStorage.getItem('restaurantEmail');

if(resEmail != undefined || resEmail != null){
  window.location.replace('./template/restaurant/home.html');
}

