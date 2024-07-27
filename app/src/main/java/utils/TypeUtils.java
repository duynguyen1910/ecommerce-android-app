package utils;

import java.util.ArrayList;

import models.TypeValue;

public class TypeUtils {

    public static ArrayList<TypeValue> setupColor() {
        String[] colorArray = {
                "Đỏ", "Cam", "Vàng", "Xanh lá", "Xanh dương",
                "Tím", "Hồng", "Tím nhạt", "Trắng", "Bạc", "Nâu",
                "Màu kaki", "Đen", "Vàng gold", "Xám tro", "Be",
                "Hồng dâu", "Nâu cafe", "Vàng đồng", "Nâu đậm",
                "Xanh lục bảo", "Xám", "Nâu nhạt", "Đen nhám", "Xanh rêu",
                "Tím than", "Trắng kem", "Xanh ngọc bích", "Xanh lá đậm",
                "Bạc ánh kim", "Xám lông chuột", "Bạch kim", "Xám khói"
        };
        ArrayList<TypeValue> colorValues = new ArrayList<>();
        for (String color : colorArray) {
            TypeValue typeValue = new TypeValue("", color);
            colorValues.add(typeValue);

        }
        return colorValues;
    }

    public static ArrayList<TypeValue> setupSizeVn() {
        String[] sizeVNArray = {
                "34", "35", "36", "37", "38", "39", "40", "41",
                "42", "43", "44", "45", "46", "47", "48", "49"
        };
        ArrayList<TypeValue> sizeVNValues = new ArrayList<>();
        for (String color : sizeVNArray) {
            TypeValue typeValue = new TypeValue(color);
            sizeVNValues.add(typeValue);

        }
        return sizeVNValues;
    }

    public static ArrayList<TypeValue> setupSizeGlobal() {
        String[] sizeGlobalArray = {
                "Free Size", "L", "M", "S", "XL", "XS", "XXL", "XXS"
        };
        ArrayList<TypeValue> sizeGlobalValues = new ArrayList<>();
        for (String color : sizeGlobalArray) {
            TypeValue typeValue = new TypeValue(color);
            sizeGlobalValues.add(typeValue);
        }
        return sizeGlobalValues;
    }

    public static ArrayList<TypeValue> setupGender() {
        String[] genderArray = {"Bé trai", "Bé gái", "Nam", "Nữ", "Unisex"};

        ArrayList<TypeValue> genderValues = new ArrayList<>();
        for (String color : genderArray) {
            TypeValue typeValue = new TypeValue(color);
            genderValues.add(typeValue);

        }
        return genderValues;
    }
}
