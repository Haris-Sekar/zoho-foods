function logout() {
    window.localStorage.clear();
    $.ajax({
        url: '../../logout',
        method: 'GET',
        error: (error) => {
            console.log("err",error);
        },
        success: (data) => {
            console.log(data);
            if(data.result === 'success'){
                window.location.replace("../../index.html");
            }
        }
    });
} 