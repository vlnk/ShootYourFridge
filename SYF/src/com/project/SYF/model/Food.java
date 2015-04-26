package com.project.SYF.model;

/**
 * Created by mimmy on 23/04/15.
 */
public class Food {
    int     id;
    int     scanID;
    String  name;

    int     quantity;
    String  brand;
    String  created_at;

    // constructors
    public Food() {
    }

    public Food(String name) {
        this.name = name;
    }

    public Food(String name, int scanID) {
        this.name = name;
        this.scanID = scanID;
    }


    public Food(String name, String brand) {
        this.name = name;
        this.brand = brand;
    }

    public Food(String name, String brand, int quantity) {
        this.name = name;
        this.brand = brand;
        this.quantity = quantity;
    }

    public Food(int id, String name, String brand) {
        this.id = id;
        this.name = name;
        this.brand = brand;
    }

    public Food(int id, String name, String brand, int quantity) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.quantity = quantity;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setScanId(int scanID) {
        this.scanID = scanID;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCreatedAt(String created_at){
        this.created_at = created_at;
    }

    // getters
    public long getId() {
        return this.id;
    }

    public long getScanId() { return this.scanID; }

    public String getName() {
        return this.name;
    }

    public String getBrand() {
        return this.brand;
    }

    public int getQuantity() {
        return this.quantity;
    }
}
