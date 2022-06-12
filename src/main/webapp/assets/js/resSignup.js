var signupPassword = document.getElementById('signupPassword');
var cpassword = document.getElementById('cpassword');
var submit = document.querySelector('#submit');
var img6 = document.getElementById('errImg6');
var imgsp = document.getElementById('errImgspe');

const name = document.getElementById('name');
const email = document.getElementById('email');
const phone = document.getElementById('phone');
const area = document.getElementById('area');
const city = document.getElementById('town');
const pinCode = document.getElementById('pinCode');
const type = document.getElementById('type');
const stime = document.getElementById('stime');
const etime = document.getElementById('etime');


if (name.value.length === 0 || email.value.length === 0 || phone.value.length === 0 || area.value.length === 0 || city.value.length === 0 || pinCode.value.length === 0 || type.value.length === 0 || stime.value.length === 0 || etime.value.length === 0) {
    // submit.addEventListener('click', registerUser);
    submit.disabled = true;
    submit.style.cursor = "not-allowed";
    submit.style.opacity = "0.5";
}

var validRegex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

email.addEventListener('keyup', function () {
    if(email.value.match(validRegex)){
        email.style.borderColor = 'green';
    }
    else{
        email.style.borderColor = 'red';
    }

});


phone.addEventListener('keyup', function () {
    if (phone.value.length === 10) {
        phone.style.borderColor = 'green';
    }
    else {
        phone.style.borderColor = 'red';
    }
});


signupPassword.addEventListener('keyup', () => {
    if (signupPassword.value.length <= 5) {
        img6.src = '../../assets/images/remove.png';
        signupPassword.style.borderColor = 'red';
        submit.disabled = true;
        submit.style.opacity = "0.5";
        submit.style.cursor = "not-allowed";
    }
    else {
        img6.src = '../../assets/images/correct.png';
        signupPassword.style.borderColor = 'green';

    }

});




cpassword.addEventListener('keyup', () => {
    if (signupPassword.value != cpassword.value) {
        cpassword.style.borderColor = 'red';
    }
    else {
        cpassword.style.borderColor = 'green';
    }
});

function passwordCheck() {
    if (signupPassword.value != cpassword.value) {
        // cpassword.setCustomValidity('Passwords must match');
        // error.innerHTML = 'Passwords must match';
        // cpassword.style.borderColor = 'red';


        toastsFactory.createToast({
            type: 'error',
            icon: 'exclamation-circle',
            message: 'Passwords must match',
            duration: 50000,
        });
        cpassword.style.borderColor = 'red';
    }
    else {
        error.innerHTML = '';
        cpassword.style.borderColor = 'green';
    }
}

document.addEventListener('keyup', ()=>{

    
    if (signupPassword.value.length > 5 && signupPassword.value === cpassword.value && email.value.length != 0 && phone.value.length != 0 && area.value.length != 0 && city.value.length != 0 && pinCode.value.length != 0 && type.value.length != 0 ) {
        submit.disabled = false;
        submit.style.opacity = "1";
        submit.style.cursor = "pointer";
    }
});



function registerUser() {
    $.ajax({
        method: 'POST',
        url: '../../RestaurantRegister',
        data: $('#registerForm').serialize(),
        success: function (data) {
            if (data.result === 'success') {
                window.localStorage.restaurantName = data.name;
                window.localStorage.restaurantEmail = data.email;
                window.location.replace("./home.html");
            }
            else {
                toastsFactory.createToast({
                    type: 'error',
                    icon: 'exclamation-circle',
                    message: data.message,
                    duration: 50000,
                });
            }
            console.log(data);
        },
        error: function (data) {
            toastsFactory.createToast({
                type: 'error',
                icon: 'exclamation-circle',
                message: data.message,
                duration: 50000,
            });

            console.log(data);
        }
    })
}