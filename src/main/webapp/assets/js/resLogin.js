function login() {
    $.ajax({
        method:'POST',
        url:'../../RestaurantLogin',
        data: $('#loginForm').serialize(),
        success: function(data) {
            if(data.result == 'success') {
                window.localStorage.restaurantName = data.name;
                window.localStorage.restaurantEmail = data.email;
                window.location.replace('./home.html');
            } else {
                $('#loginError').text(data.message);
            }
        }
    })
}

var showPassword = document.getElementById('showPassword');
showPassword.addEventListener('click', function () {
    var password = document.getElementById('password');
    if (password.type === 'password') {
        password.type = 'text';
    } else {
        password.type = 'password';
    }
}); 
