package models;

public class RestaurantModel {
    String name;
    String email;
    String phone;
    String area;
    String town;
    String state;
    String pinCode;
    String resType;
    String resStartTime;
    String resEndTime;
    String password;
    String result;
    int id;
    int foodCount;
    float rating;
    String image;
    Boolean isActive;

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getRating() {
        return this.rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getFoodCount() {
        return this.foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTown() {
        return this.town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPinCode() {
        return this.pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getResType() {
        return this.resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    public String getResStartTime() {
        return this.resStartTime;
    }

    public void setResStartTime(String resStartTime) {
        this.resStartTime = resStartTime;
    }

    public String getResEndTime() {
        return this.resEndTime;
    }

    public void setResEndTime(String resEndTime) {
        this.resEndTime = resEndTime;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
