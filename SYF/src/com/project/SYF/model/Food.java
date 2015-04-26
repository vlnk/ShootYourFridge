package com.project.SYF.model;

/**
 * Created by mimmy on 23/04/15.
 */
public class Food {
    int     id;
    String     scanID;
    String  name;

    // constructors
    public Food() {}

    public Food(String name) {
        this.name = name;
    }

    public Food(String name, String scanID) {
        this.name = name;
        this.scanID = scanID;
    }


    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setScanId(String scanID) {
        this.scanID = scanID;
    }

    public void setName(String name) {
        this.name = name;
    }


    // getters
    public long getId() {
        return this.id;
    }

    public String getScanId() { return this.scanID; }

    public String getName() {
        return this.name;
    }

}
