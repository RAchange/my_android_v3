package com.example.myapplicationrecycle_view;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class find_user {
    String mysql_ip = "193.42.40.110";
    int mysql_port = 3306; // Port 預設為 3306
    String db_name = "MedBigDataAI";
    String url = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name;
    String db_user = "medical";
    String db_password = "ggininder";

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
    public int  getData(String[] name_list) {
        String data = "";
        int count=0;
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT * FROM patient_info;";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int id_index = rs.findColumn("id");
            int name_index = rs.findColumn("name");
            while (rs.next())
            {
                name_list[count] = rs.getString(name_index);
                count++;
            }
            st.close();
        } catch (SQLException e) {
            System.out.println("錯爛");
        }
        return count;
    }
}
