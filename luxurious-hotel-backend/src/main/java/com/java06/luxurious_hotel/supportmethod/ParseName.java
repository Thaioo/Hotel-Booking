package com.java06.luxurious_hotel.supportmethod;

public class ParseName {

    public static String[] parseName(String fullName) {
        String firstName = "";
        String lastName = "";

        // Kiểm tra chuỗi có khoảng trắng hay không
        int spaceIndex = fullName.indexOf(" ");
        if (spaceIndex != -1) {
            // Nếu có khoảng trắng, tách chuỗi
            firstName = fullName.substring(0, spaceIndex).trim();
            lastName = fullName.substring(spaceIndex + 1).trim();
        } else {
            // Nếu không có khoảng trắng, set fullName làm lastName
            lastName = fullName.trim();
        }

        return new String[]{firstName, lastName};
    }
}
