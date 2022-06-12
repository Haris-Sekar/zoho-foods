$.ajax({
    method: 'GET',
    url: '../../RestaurantValidation',
    error: (error) => {
        console.log("err", error);
    },
    success: (data) => {
        console.log(data);
        if (data.result === 'failure') {
            window.localStorage.clear();
            window.location.replace("../../index.html");
        }
    }
}) 