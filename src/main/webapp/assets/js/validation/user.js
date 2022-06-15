$.ajax({
    method: 'GET',
    url: '../UserValidation',
    error: (error) => {
        console.log("err", error);
    },
    success: (data) => {
        console.log(data);
        if (data.result === 'failure') {
            window.localStorage.clear();
            window.location.replace("../index.html");
        }
        else {
            renderProfileImg(data.profilePic);
        }
    }
}) 

const imgCon = document.getElementById('profilePic');
	const dropDown = document.getElementById('dropDown');
	imgCon.addEventListener('mouseenter', function () {
		 
			dropDown.style.display = "block";
		 
	});
	imgCon.addEventListener('mouseleave', function () {
		 
			dropDown.style.display = "none";
		 
	});
function renderProfileImg(data){
    const profileImg = document.getElementById("imgConProfile");
    profileImg.innerHTML = `<img src="../uploads/profilePic/${data}" alt="" id="profileImg">`
}