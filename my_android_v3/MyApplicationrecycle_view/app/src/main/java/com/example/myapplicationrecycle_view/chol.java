package com.example.myapplicationrecycle_view;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class chol extends AppCompatActivity {
    private Button return_to_uppage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);
        Intent intent = getIntent();

        int[] chol_list = intent.getIntArrayExtra("extra_data");
        String id = intent.getStringExtra("id_data");
        String name = intent.getStringExtra("name_data");
        TextView title = (TextView)findViewById(R.id.textView2);
        String title_name = name + "的膽固醇變化";
        title.setText(title_name);
        return_to_uppage = findViewById(R.id.button);
        return_to_uppage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chol.this,MainActivity2.class);
                intent.putExtra("id_data",id);
                intent.putExtra("name_data",name);
                startActivity(intent);
            }
        });
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);


        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();

        LocalDate now = LocalDate.now();
        String month = Integer.toString(now.getMonthValue());
        int maxn = Integer.min( chol_list.length, 10);
        System.out.println(chol_list.length);
        int chol_value = 0;
        for(int i=10;i>0;i--){
            String day = Integer.toString(i);
            day = day + "days ago";
            if(i >= chol_list.length){
                data.add(new ValueDataEntry(day,230));
                chol_value = chol_value + 230;
                continue;
            }
            data.add(new ValueDataEntry(day, chol_list[i]));
            chol_value = chol_value + chol_list[i];
        }
        chol_value = chol_value/10;
        TextView note = (TextView)findViewById(R.id.textView3);
        if(chol_value >= 240){
            note.setText("此人應注意高膽固醇");
        }
        /*data.add(new ValueDataEntry("Rouge", 80540));
        data.add(new ValueDataEntry("Foundation", 94190));
        data.add(new ValueDataEntry("Mascara", 102610));
        data.add(new ValueDataEntry("Lip gloss", 110430));
        data.add(new ValueDataEntry("Lipstick", 128000));
        data.add(new ValueDataEntry("Nail polish", 143760));
        data.add(new ValueDataEntry("Eyebrow pencil", 170670));
        data.add(new ValueDataEntry("Eyeliner", 213210));
        data.add(new ValueDataEntry("Eyeshadows", 249980));*/

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("10天內膽固醇變化量");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("day");
        cartesian.yAxis(0).title("value");

        anyChartView.setChart(cartesian);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.one_month:
                Intent intent = new Intent(chol.this,chol_30day.class);
                intent.putExtra("id_data",id);
                intent.putExtra("name_data",name);
                intent.putExtra("extra_data",chol_list);
                startActivity(intent);
                break;
            case R.id.ten_days:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.time_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
