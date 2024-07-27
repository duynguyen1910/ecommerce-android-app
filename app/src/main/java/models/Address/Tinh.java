package models.Address;

public class Tinh {
    public String id;
    public String full_name;

    public Tinh(String id, String full_name) {
        this.id = id;
        this.full_name = full_name;
    }

    public Tinh() {
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
