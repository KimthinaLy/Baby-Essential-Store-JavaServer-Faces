/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class Occasion {
    private int occasionId;
    private String name;

    public Occasion() {
    }

    public Occasion(int occasionId, String name) {
        this.occasionId = occasionId;
        this.name = name;
    }

    public int getOccasionId() {
        return occasionId;
    }

    public void setOccasionId(int occasionId) {
        this.occasionId = occasionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
