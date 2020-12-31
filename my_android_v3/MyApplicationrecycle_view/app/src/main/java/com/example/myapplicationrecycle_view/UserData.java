package com.example.myapplicationrecycle_view;

import java.util.HashMap;

public class UserData {
    String username;
    HashMap<String, Data_Information> Datas;
    boolean Data_getted;

    public UserData(String username) {
        this.username = username;
        this.Datas = new HashMap<>();
        this.Data_getted = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
