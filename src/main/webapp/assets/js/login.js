var showPassword = document.getElementById('showPassword');
showPassword.addEventListener('click', function () {
    var password = document.getElementById('password');
    if (password.type === 'password') {
        password.type = 'text';
    } else {
        password.type = 'password';
    }
}); 

//login api call
function login() {

    $.ajax({
        method: 'POST',
        url: '../login',
        data: $('#loginForm').serialize(),
        error: (error) => {
            console.log(error);
        },
        success: (data) => {
            console.log(data);
            if(data.result === 'success'){ 
                window.localStorage.email = data.email;
                window.localStorage.name = data.name;
                window.location.replace("../index.html");
            }
            else if(data.result === 'failure'){
                
            } 
        }

    })
}