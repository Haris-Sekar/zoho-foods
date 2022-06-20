function fetchUserDetails() {
  $.ajax({
    method: "GET",
    url: "../../Restaurant/forProfile",
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

const name1 = document.getElementById("name");
const email = document.getElementById("email");
const phone = document.getElementById("phone");
const area = document.getElementById("area");
const town = document.getElementById("town");
const state = document.getElementById("state");
const pinCode = document.getElementById("pinCode");
const stime = document.getElementById("stime");
const etime = document.getElementById("etime");
const radioI = document.getElementById("radio-i");
const radioJ = document.getElementById("radio-j");
const profilePic = document.getElementById("profilePic1");
const fileName = document.getElementById("fileName");
function renderDetails(data) {
  console.log(data);
  name1.value = data.name;
  email.value = data.email;
  phone.value = data.phone;
  fileName.value = data.image;
  area.value = data.area;
  town.value = data.town;
  state.value = data.state;
  pinCode.value = data.pinCode;
  stime.value = data.resStartTime;
  etime.value = data.resEndTime;
  if(data.isActive) radioJ.checked = true;
  else radioI.checked = true;
  profilePic.innerHTML = `<img src="../../uploads/resPic/${data.image}" alt="">`;
}

function profileChange() {
  alert1.hidden = false;
}

function updateProfilePic() {  
  alert("asdf");
  var formData1 = $("#editProfilePic")[0];
  const formData = new FormData(formData1);
  formData.append("file", file.files[0]);
  $.ajax({
    method: "POST",
    ecnType: "multipart/form-data",
    url: "../../Restaurant/updateProfilePic",
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
const loadingWrapper = document.getElementById("loading-wrapper");
save.addEventListener("click", (event) => { 
    loadingWrapper.hidden = false;
    $.ajax({
      method: "POST",
      url: "../../Restaurant/updateRestaurant",
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
    img6.src = "../../assets/images/remove.png";
    password.style.borderColor = "red";
    change.style.opacity = "0.5";
    change.style.cursor = "not-allowed";
    passwordErr = true;
  } else {
    img6.src = "../../assets/images/correct.png";
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
      method: "POST",
      url: "../../Restaurant/updatePassword",
      data: $("#passwordChangeForm").serialize(),
      success: (data) => { 
        console.log(data);
        toastsFactory.createToast({
          type: "success",
          icon: "check-circle",
          message: "Password Updated Successfully",
          duration: 2000,
        });  
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
});

function editProfile1() {
  editProfile.style.backgroundColor = "#f1f1f1";
  editProfile.style.borderLeft = "5px solid black";
  passwordChange.style.backgroundColor = "#ffffff";
  passwordChange.style.borderLeft = "none";
  userEdit.hidden = false;
  passwordCon.hidden = true;
}
