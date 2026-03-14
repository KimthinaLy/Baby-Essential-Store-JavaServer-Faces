/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import org.apache.tika.Tika;
import java.util.Base64;
import java.util.List;

/**
 *
 * @author Admin
 */
public class Product {

    private int productId;
    private String name;
    private double price;
    private int quantityOnHand;
    private byte[] image;
    private int categoryId;
    private String description;
    private String categoryName;
    private List<String> occasions;

    public Product() {
    }

    public Product(int id, String name, double price, int quantityOnHand, byte[] image, int categoryId, String description, String categoryName, List<String> occasions) {
        this.productId = id;
        this.name = name;
        this.price = price;
        this.quantityOnHand = quantityOnHand;
        this.image = image;
        this.categoryId = categoryId;
        this.description = description;
        this.categoryName = categoryName;
        this.occasions = occasions;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getOccasions() {
        return occasions;
    }

    public void setOccasions(List<String> occasions) {
        this.occasions = occasions;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(int quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductImage() {
        if (image == null) {
            return "/resources/images/logo2.png"; // fallback
        }

        Tika tika = new Tika();
        String mimeType;
        try {
            mimeType = tika.detect(image);
        } catch (Exception e) {
            mimeType = "image/jpeg"; // fallback
        }

        String base64 = Base64.getEncoder().encodeToString(image);
        return "data:" + mimeType + ";base64," + base64;
    }

    @Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Product)) return false;
    Product other = (Product) o;
    return this.productId == other.productId; // compare primitive ints directly
}

@Override
public int hashCode() {
    return Integer.hashCode(productId);
}

}
