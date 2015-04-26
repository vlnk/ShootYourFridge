package com.project.SYF.model;

/**
 * Created by mimmy on 26/04/15.
 */
public class Recipe {

    int     id;
    String  name;
    String  details;
    String  description;
    String  href;

    public Recipe() {}

    public Recipe(String name, String details, String description,
                  String href){
        this.name = name;
        this.description = description;
        this.href = href;
        this.details = details;
    }


    // getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDetails() { return details; }
    public String getDescription() { return description; }
    public String getHref() { return href; }


    // setters
    public void setHref(String href) { this.href = href; }
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name;  }
    public void setDetails(String details) { this.details = details; }

    public void setDescription(String description) {
        this.description =  description;
    }



}
