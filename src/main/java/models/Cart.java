package models;

public class Cart {
    private int id;
    private String name;
    private String description;
    private String image;
    private String categoryName;
    private float price;
    private float discount;
    private int quantity;
    private String res_name;
    private String area;
    private String town;
    private String type;
    private int res_id;
    private int userId;
    private String foodType;

    public int getRes_id() {
        return this.res_id;
    }

    public void setRes_id(int res_id) {
        this.res_id = res_id;
    }

    public String getRes_name() {
        return this.res_name;
    }

    public void setRes_name(String res_name) {
        this.res_name = res_name;
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

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFoodType() {
        return this.foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscount() {
        return this.discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
