package com.example.myapplicationrecycle_view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Area;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.util.ArrayList;
import java.util.List;

public class Visualization_Page extends AppCompatActivity {

    AnyChartView Data_Visualization;
    TextView Vs_pre_page_Button, Logout_Button;
    TextView Note;
    Intent Userinfo_intent;
    Integer StartIndex, EndIndex;
    String ChartType;
    Data_Information Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization__page);

        Userinfo_intent = getIntent();
        Data = Userinfo_intent.getParcelableExtra("Data");
        ChartType = Userinfo_intent.getStringExtra("ChartType");
        StartIndex = Userinfo_intent.getIntExtra("StartIndex", 0);
        EndIndex = Userinfo_intent.getIntExtra("EndIndex", 0);

        Data_Visualization = findViewById(R.id.Data_Visualization);
        Vs_pre_page_Button = findViewById(R.id.Return_to_UserInfo);
        Logout_Button = findViewById(R.id.Vs_Logout);
        Note = findViewById(R.id.Note);

        Boolean Too_High = false;

        for(int i = StartIndex; i < EndIndex; i++){
            if(Data.list.get(i).Value > Data.HighValue){
                Too_High = true;
                Note.setText("此人應注意高" + Data.Name);
                break;
            }
        }

        if(Too_High == false){
            Note.setText( Data.Name + "於" + Data.list.get(StartIndex).Date + "至" + Data.list.get(EndIndex - 1).Date + "正常");
        }

        if(ChartType.equals("長條圖")){
            setBarChart();
        }else if(ChartType.equals("折線圖")){
            setLineChart();
        }else if(ChartType.equals("面積圖")){
            setAreaChart();
        }

        Vs_pre_page_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Logout_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Visualization_Page.this, login_main.class);
                startActivity(intent);
            }
        });
    }

    public void setBarChart(){
        Cartesian cartesian = AnyChart.column();

        List<DataEntry> ShowData = new ArrayList<>();
        if(Data.list.size() < 30){
            Toast.makeText(Visualization_Page.this, "不足30筆資料" + Integer.toString(Data.list.size()), Toast.LENGTH_SHORT).show();
        }else {
            for (int i = StartIndex; i < EndIndex; i++) {
                ShowData.add(new ValueDataEntry(Data.list.get(i).Date, Data.list.get(i).Value));
            }
        }

        Column column = cartesian.column(ShowData);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title(Data.list.get(StartIndex).Date + " ~ " + Data.list.get(EndIndex - 1).Date + "的" + Data.Name + "變化量");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xScroller(true);
        cartesian.xZoom().setToPointsCount(6, false, null);
        cartesian.xZoom(true);

        cartesian.xAxis(0).title("日期");
        cartesian.yAxis(0).title(Data.Unit);

        Data_Visualization.setChart(cartesian);
    }

    public void setLineChart(){
        Cartesian cartesian = AnyChart.line();

        List<DataEntry> ShowData = new ArrayList<>();
        if(Data.list.size() < 30){
            Toast.makeText(Visualization_Page.this, "不足30筆資料", Toast.LENGTH_SHORT).show();
        }else {
            for (int i = StartIndex; i < EndIndex; i++) {
                ShowData.add(new ValueDataEntry(Data.list.get(i).Date, Data.list.get(i).Value));
            }
        }

        Line line = cartesian.line(ShowData);
        cartesian.xGrid(true);
        cartesian.yGrid(true);

        line.stroke();
        line.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        line.tooltip().background().stroke();
        cartesian.marker(ShowData);

        cartesian.animation(true);
        cartesian.title(Data.list.get(StartIndex).Date + " ~ " + Data.list.get(EndIndex - 1).Date + "的" + Data.Name + "變化量");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xScroller(true);
        cartesian.xZoom().setToPointsCount(6, false, null);
        cartesian.xZoom(true);

        cartesian.xAxis(0).title("日期");
        cartesian.yAxis(0).title(Data.Unit);

        Data_Visualization.setChart(cartesian);
    }

    public void setAreaChart(){
        Cartesian cartesian = AnyChart.area();

        List<DataEntry> ShowData = new ArrayList<>();
        if(Data.list.size() < 30){
            Toast.makeText(Visualization_Page.this, "不足30筆資料", Toast.LENGTH_SHORT).show();
        }else {
            for (int i = StartIndex; i < EndIndex; i++) {
                ShowData.add(new ValueDataEntry(Data.list.get(i).Date, Data.list.get(i).Value));
            }
        }

        Area area= cartesian.area(ShowData);
        cartesian.xGrid(true);
        cartesian.yGrid(true);

        area.stroke();
        area.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        area.tooltip().background().stroke();
        cartesian.marker(ShowData);

        cartesian.animation(true);
        cartesian.title(Data.list.get(StartIndex).Date + " ~ " + Data.list.get(EndIndex - 1).Date + "的" + Data.Name + "變化量");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xScroller(true);
        cartesian.xZoom().setToPointsCount(6, false, null);
        cartesian.xZoom(true);

        cartesian.xAxis(0).title("日期");
        cartesian.yAxis(0).title(Data.Unit);

        Data_Visualization.setChart(cartesian);
    }
}