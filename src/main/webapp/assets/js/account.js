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

const name = document.getElementById("name");
const email = document.getElementById("email");
const phone = document.getElementById("phone");
const profilePic = document.getElementById("profilePic1");

function renderDetails(data) {
  name.value = data.name;
  email.value = data.email;
  phone.value = data.phone;
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

