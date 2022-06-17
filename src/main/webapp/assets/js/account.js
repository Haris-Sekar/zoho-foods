function fetchUserDetails() {
  $.ajax({
    method: "GET",
    url: "../login",
    success: (data) => {
      console.log(data);
      userData = data;
      renderDetails(data);
    },
    error: (err) => {
      console.log(err);
    },
  });
}

var userData;
fetchUserDetails();
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

const name1 = document.getElementById("name");
const email = document.getElementById("email");
const phone = document.getElementById("phone");
const profilePic = document.getElementById("profilePic1");
const fileName = document.getElementById("fileName");
function renderDetails(data) {
  name1.value = data.name;
  email.value = data.email;
  phone.value = data.phone;
  fileName.value = data.profilePic;
  profilePic.innerHTML = `<img src="../uploads/profilePic/${data.profilePic}" alt="">`;
}

function profileChange() {
  alert1.hidden = false;
}

function updateProfilePic() {
  const fileUpName = document.getElementById("fileUpName");
  const fileUpEmail = document.getElementById("fileUpEmail");
  const fileUpPhone = document.getElementById("fileUpPhone");
  const fileUpType = document.getElementById("fileUpType");
  fileUpType.value = "edit";
  fileUpName.value = userData.name;
  fileUpEmail.value = userData.email;
  fileUpPhone.value = userData.phone;
  var formData1 = $("#editProfilePic")[0];
  const formData = new FormData(formData1);
  formData.append("file", file.files[0]);
  $.ajax({
    method: "POST",
    ecnType: "multipart/form-data",
    url: "../login",
    data: formData,
    cache: false,
    processData: false,
    contentType: false,
    success: (data) => {
      console.log(data);
      alert1.hidden = true;
      toastsFactory.createToast({
        type: "success",
        icon: "check-circle",
        message: "Profile Pic Updated Successfully",
        duration: 2000,
      });
      profilePic.innerHTML = `<img src="../uploads/profilePic/${data}" alt="">`;
    },
    error: (err) => {
      console.log(err);
    },
  });
}

const save = document.getElementById("save");
const change = document.getElementById("change");
var changed;
name1.addEventListener("keyup", (event) => {
  if (event.target.value != userData.name) {
    save.style.opacity = 1;
    save.style.cursor = "pointer";
    changed = true;
  } else {
    save.style.opacity = 0.5;
    save.style.cursor = "not-allowed";
    changed = false;
  }
});

email.addEventListener("keyup", (event) => {
  if (event.target.value != userData.email) {
    save.style.opacity = 1;
    save.style.cursor = "pointer";
    changed = true;
  } else {
    save.style.opacity = 0.5;
    save.style.cursor = "not-allowed";
    changed = false;
  }
});

phone.addEventListener("keyup", (event) => {
  if (event.target.value != userData.phone) {
    save.style.opacity = 1;
    save.style.cursor = "pointer";
    changed = true;
  } else {
    save.style.opacity = 0.5;
    save.style.cursor = "not-allowed";
    changed = false;
  }
});

const loadingWrapper = document.getElementById("loading-wrapper");
save.addEventListener("click", (event) => {
  if (changed) {
    loadingWrapper.hidden = false;
    $.ajax({
      method: "POST",
      url: "../login",
      data: $("#updateForm").serialize(),
      success: (data) => {
        console.log(data);
        loadingWrapper.hidden = true;
        toastsFactory.createToast({
          type: "success",
          icon: "check-circle",
          message: "Profile Updated Successfully",
          duration: 2000,
        });

        fetchUserDetails();
        save.style.opacity = 0.5;
        save.style.cursor = "not-allowed";
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
});
const editProfile = document.getElementById("editProfile");
const passwordChange = document.getElementById("passwordChange");
const passwordCon = document.getElementById("passwordCon");
const userEdit = document.getElementById("userEdit");
function passwordChange1() {
  editProfile.style.backgroundColor = "#ffffff";
  editProfile.style.borderLeft = "none";
  passwordChange.style.backgroundColor = "#f1f1f1";
  passwordChange.style.borderLeft = "5px solid black";
  userEdit.hidden = true;
  passwordCon.hidden = false;
}

const password = document.getElementById("password");
const conPassword = document.getElementById("conPassword");

const img6 = document.getElementById("errImg6");

var passwordErr = false;

password.addEventListener("keyup", () => {
  if (password.value.length <= 5) {
    img6.src = "../assets/images/remove.png";
    password.style.borderColor = "red";
    change.style.opacity = "0.5";
    change.style.cursor = "not-allowed";
    passwordErr = true;
  } else {
    img6.src = "../assets/images/correct.png";
    password.style.borderColor = "green";
    change.style.opacity = "1";
    change.style.cursor = "pointer";
    passwordErr = false;
  }
  if (password.value != conPassword.value) {
    conPassword.style.borderColor = "red";
    change.style.opacity = "0.5";
    change.style.cursor = "not-allowed";
    passwordErr = true;
  } else {
    conPassword.style.borderColor = "green";
    change.style.opacity = "1";
    change.style.cursor = "pointer";
    passwordErr = false;
  }
});

conPassword.addEventListener("keyup", () => {
  if (password.value != conPassword.value) {
    conPassword.style.borderColor = "red";
    change.style.opacity = "0.5";
    change.style.cursor = "not-allowed";
    passwordErr = true;
  } else {
    conPassword.style.borderColor = "green";
    change.style.opacity = "1";
    change.style.cursor = "pointer";
    passwordErr = false;
  }
});

change.addEventListener("click", () => {
  if (!passwordErr) {
    $.ajax({
      method:'POST',
      url:'../Password',
      data: $("#passwordChangeForm").serialize(),
      success: (data) => {
        console.log(data);
      },
      error: (err) => {
        console.log(err);
      }
    });
  }
});

function editProfile1(){
  editProfile.style.backgroundColor = "#f1f1f1";
  editProfile.style.borderLeft = "5px solid black";
  passwordChange.style.backgroundColor = "#ffffff";
  passwordChange.style.borderLeft = "none";
  userEdit.hidden = false;
  passwordCon.hidden = true;
  
}