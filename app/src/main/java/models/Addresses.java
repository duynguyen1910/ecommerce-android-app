package models;

import java.io.Serializable;

public class Addresses implements Serializable {
    private String id;
    private String full_name;

    public Addresses(String ID, String name) {
        this.id = id;
        this.full_name = name;
    }

    public String getID() {
        return id;
    }

    public void setID(String ID) {
        this.id = id;
    }

    public String getName() {
        return full_name;
    }

    public void setName(String name) {
        this.full_name = name;
    }

    @Override
    public String toString() {
        return full_name;
    }
}
