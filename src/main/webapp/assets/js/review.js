function getReview() {
  $.ajax({
    method: "GET",
    url: "../../Review/getReview",
    success: function (data) {
      console.log(data);
      renderReview(data);
    },
    error: function (data) {
      console.log(data);
    },
  });
}

getReview();
const review = document.getElementById("review");
function renderReview(data) {
  data.forEach((e) => {
    var date = new Date(e.date);
    const timestamp =
      date.toLocaleDateString() +
      " " +
      date.toLocaleString([], { hour: "2-digit", minute: "2-digit" });
    const time = timestamp;
    review.innerHTML += `
        <div class="reviewCard">
        <div class="profile">
            <div class="imgCon" id="imgCon"><img src="../../uploads/profilePic/${
              e.profilePic
            }" alt="" srcset=""></div>
            <div class="det">
                <div class="name" id="name">${e.name}</div>
                <div class="name" id="name">${e.email}</div>
                <div class="name" id="name">${time}</div>
            </div>
        </div>
        <div class="rating">
            <div class="star-ratings">
                <div class="fill-ratings" style="width: ${e.rating * 20}%;">
                  <span>★★★★★</span>
                </div>
                <div class="empty-ratings">
                  <span>★★★★★</span>
                </div>
              </div>
        </div>
        <div class="review">${e.review}</div>
    </div>
        `;
  });
}
