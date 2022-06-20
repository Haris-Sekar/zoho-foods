function adminLogin() {
  $.ajax({
    method: "POST",
    url: "../Admin/login",
    data: $("#loginForm").serialize(),
    success: (data) => {
      console.log(data);
      if (data.result === "success") {
        window.location.replace("./index.html");
      } else {
        toastsFactory.createToast({
          type: "error",
          icon: "exclamation-circle",
          message: "Invalid email or password",
          duration: 5000,
        });
      }
    },
    error: (err) => {
      console.log(err);
    },
  });
}
