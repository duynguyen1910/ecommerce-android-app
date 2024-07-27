package models.Address;

public class Quan {
    public String id;
    public String full_name;

    public Quan(String id, String full_name) {
        this.id = id;
        this.full_name = full_name;
    }

    public Quan() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}
