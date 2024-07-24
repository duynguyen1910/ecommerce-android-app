package models;

import java.util.ArrayList;

public class Type {
    private String typeName;
    private ArrayList<TypeValue> listValues;

    public Type(String typeName, ArrayList<TypeValue> listValues) {
        this.typeName = typeName;
        this.listValues = listValues;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public ArrayList<TypeValue> getListValues() {
        return listValues;
    }

    public void setListValues(ArrayList<TypeValue> listValues) {
        this.listValues = listValues;
    }
}
