package com.example.myapplicationrecycle_view;


public class thread_find_user extends Thread {
    String[] name_list = new String[80];
    int count = 0;
    @Override
    public void run(){
        find_user con = new find_user();
        con.run();
        count = con.getData(name_list);
    }
    public String[] get_name(){
        return name_list;
    }
    public int get_student_number(){
        return count;
    }
}
