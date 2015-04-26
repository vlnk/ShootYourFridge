package com.project.SYF.model;

/**
 * Created by mimmy on 26/04/15.
 */
public class Catalog {
    int     id;
    String  scanID;
    String  name;

    // constructors
    public Catalog() {
    }

    public Catalog(String name, String scanID) {
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
