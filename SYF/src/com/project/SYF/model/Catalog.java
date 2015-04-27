package com.project.SYF.model;

public class Catalog {
    private int     id;
    private String  scanID;
    private String  name;

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
