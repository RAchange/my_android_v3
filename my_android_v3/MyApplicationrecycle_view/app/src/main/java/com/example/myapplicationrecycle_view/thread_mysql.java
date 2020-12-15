package com.example.myapplicationrecycle_view;

public class thread_mysql extends Thread{
    String[] chol_list = new String[100];
    String[] blood_pressure_list = new String[100];
    String[] thalach_list = new String[100];
    String count;
    String back_id;
    @Override
    public void run(){
        mysql con = new mysql();
        con.run();
        con.give_id(back_id);
        chol_list = con.getData_chol();
        //System.out.println("pp"  +  chol_list[3]);
        blood_pressure_list = con.getData_blood_pressure();
        thalach_list = con.getData_thalach();
    }
    public void give_id(String id){//id傳給資料庫
        back_id = id;
        return;
    }
    public String[] get_chol_list(){
        return chol_list;
    }
    public  String[] getBlood_pressure_list(){
        return  blood_pressure_list;
    }
    public  String[] getThalach_list(){
        return thalach_list;
    }
}
