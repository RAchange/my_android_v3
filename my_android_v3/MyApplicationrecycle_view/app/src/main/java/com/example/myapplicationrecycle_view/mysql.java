package com.example.myapplicationrecycle_view;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class mysql {
    // 資料庫定義
    String mysql_ip = "193.42.40.110";
    int mysql_port = 3306; // Port 預設為 3306
    String db_name = "MedBigDataAI";
    String url = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name;
    String db_user = "medical";
    String db_password = "ggininder";
    String back_id;
    public void give_id(String id){//得到前端的資料庫
        back_id = id;
        return;
    }


    public void run() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("DB","加載驅動成功");
        }catch( ClassNotFoundException e) {
            Log.e("DB","加載驅動失敗");
            return;
        }

        // 連接資料庫
        try {
            Connection con = DriverManager.getConnection(url,db_user,db_password);
            Log.v("DB","遠端連接成功");
        }catch(SQLException e) {
            Log.e("DB","遠端連接失敗");
            Log.e("DB", e.toString());
            e.printStackTrace();
            System.out.println(url);
        }
    }
    public String[] getData_chol() {//得到膽固醇
        String sql = "SELECT * from heart where id=";
        String[] chol_list = new String[100];
        sql = sql + back_id;
        System.out.println(sql);
        try {
            int count=0;
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int chol_index = rs.findColumn("chol");
            while (rs.next())
            {
                chol_list[count] = rs.getString(chol_index);
                count++;
            }
            st.close();
        } catch (SQLException e) {
            System.out.println("錯爛");
        }
        return chol_list;
    }
    public String[] getData_blood_pressure() {//得到血壓
        String sql = "SELECT * from heart where id=";
        String[] blood_pressure_list = new String[100];
        sql = sql + back_id;
        System.out.println(sql);
        try {
            int count=0;
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int chol_index = rs.findColumn("trestbps");
            while (rs.next())
            {
                blood_pressure_list[count] = rs.getString(chol_index);
                count++;
            }
            st.close();
        } catch (SQLException e) {
            System.out.println("錯爛");
        }
        return blood_pressure_list;
    }
    public String[] getData_thalach() {//得到血壓
        String sql = "SELECT * from heart where id=";
        String[] thalach_list = new String[100];
        sql = sql + back_id;
        System.out.println(sql);
        try {
            int count=0;
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int chol_index = rs.findColumn("thalach");
            while (rs.next())
            {
                thalach_list[count] = rs.getString(chol_index);
                count++;
            }
            st.close();
        } catch (SQLException e) {
            System.out.println("錯爛");
        }
        return thalach_list;
    }
}
