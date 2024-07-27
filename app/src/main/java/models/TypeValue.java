package models;

import java.io.Serializable;

public class TypeValue implements Serializable {
    private String image;
    private String value;
    private boolean isChecked;


    public TypeValue(String image, String value) {
        this.image = image;
        this.value = value;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
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
