const alert1 = document.getElementById("alert");

function clearCart() {
  $.ajax({
    url: "../Cart?clear=1",
    type: "GET",
    success: (data) => {
      console.log(data);
      cartNo.innerHTML = 0;
      toastsFactory.createToast({
        type: "success",
        icon: "check-circle",
        message: "Cart cleared",
        duration: 500000,
      });
      setTimeout(() => {
        alert1.hidden = true;
      }, 500);
      
    },
    error: (err) => {
      console.log(err);
    },
  });
}


function closeAlert() {
    alert1.hidden = true;
}