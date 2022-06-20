//signup password check

var signupPassword = document.getElementById("signupPassword");
var cpassword = document.getElementById("cpassword");
var submit = document.querySelector("#submit");
var img6 = document.getElementById("errImg6");
var imgsp = document.getElementById("errImgspe");
var phone = document.getElementById("phone");
var email = document.getElementById("email");
var name1 = document.getElementById("name");
var address = document.getElementById("address");

if (
  name1.value.length === 0 ||
  email.value.length === 0 ||
  phone.value.length === 0 ||
  address.value.length === 0
) {
  submit.disabled = true;
  submit.style.cursor = "not-allowed";
  submit.style.opacity = "0.5";
}

var validRegex =
  /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

email.addEventListener("keyup", function () {
  if (email.value.match(validRegex)) {
    email.style.borderColor = "green";
  } else {
    email.style.borderColor = "red";
  }
});

phone.addEventListener("keyup", function () {
  if (phone.value.length === 10) {
    phone.style.borderColor = "green";
  } else {
    phone.style.borderColor = "red";
  }
});

signupPassword.addEventListener("keyup", () => {
  if (signupPassword.value.length <= 5) {
    img6.src = "../assets/images/remove.png";
    signupPassword.style.borderColor = "red";
    submit.disabled = true;
    submit.style.opacity = "0.5";
    submit.style.cursor = "not-allowed";
  } else {
    img6.src = "../assets/images/correct.png";
    signupPassword.style.borderColor = "green";
  }
});

cpassword.addEventListener("keyup", () => {
  if (signupPassword.value != cpassword.value) {
    cpassword.style.borderColor = "red";
  } else {
    cpassword.style.borderColor = "green";
  }
});

function passwordCheck() {
  if (signupPassword.value != cpassword.value) {
    toastsFactory.createToast({
      type: "error",
      icon: "exclamation-circle",
      message: "Passwords must match",
      duration: 50000,
    });
    cpassword.style.borderColor = "red";
  } else {
    cpassword.style.borderColor = "green";
  }
}

document.addEventListener("keyup", () => {
  if (
    signupPassword.value.length > 5 &&
    signupPassword.value === cpassword.value &&
    email.value.length != 0 &&
    phone.value.length != 0 &&
    address.value.length != 0 &&
    name1.value.length != 0
  ) {
    submit.disabled = false;
    submit.style.opacity = "1";
    submit.style.cursor = "pointer";
  }
});

//getting userData

function registerUser() {
  var formData = $("#registerForm")[0];
  var formData1 = new FormData(formData);
  formData1.append("file", file.files[0]);
  $.ajax({
    method: "POST",
    ecnType: "multipart/form-data",
    url: "../register",
    data: formData1,
    cache: false,
    processData: false,
    contentType: false,
    error: (error) => {
      console.log(error);
    },
    success: (data) => {
      console.log(data);
      if (data.result === "success") {
        window.localStorage.name = data.name;
        window.localStorage.email = data.email;
        window.location.replace("../index.html");
      }
    },
  });
}
