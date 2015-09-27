package com.contactshare.models;

import com.contactshare.utilities.Constants;

public class VCard {
    String name;
    String number;
    String email;

    public VCard(String name, String number, String email) {
        this.name = name;
        this.number = number;
        this.email = email;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(Constants.KEY_NAME);
        str.append(":");
        str.append(name);
        str.append(";");
        str.append(Constants.KEY_NUMBER);
        str.append(":");
        str.append(number);
        str.append(";");
        str.append(Constants.KEY_EMAIL);
        str.append(":");
        str.append(email);
        str.append(";");
        return str.toString();
    }
}
