package models;

public class TypeValue {
    private String image;
    private String value;

    public TypeValue(String image, String value) {
        this.image = image;
        this.value = value;
    }

    public TypeValue(String value) {
        this.value = value;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
