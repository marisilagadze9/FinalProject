package com.example.finalproject;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.List;

public class Statistic extends AppCompatActivity {
    Spinner spinMonth,spinType;
    PieChart pie;
    BarChart bar;
    TextView totalAmount;

    KomunaluriDB db;
    List<Komunaluri> allList=new ArrayList<>();

    String[] months = {"ყველა","იანვარი","თებერვალი","მარტი","აპრილი","მაისი",
            "ივნისი","ივლისი","აგვისტო","სექტემბერი","ოქტომბერი","ნოემბერი","დეკემბერი"};

    String[] types = {"ყველა","დენი","გაზი","წყალი","დასუფთავება","ტელევიზია/ინტერნეტი","სხვა"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistic);

        spinMonth=findViewById(R.id.spinnerMonth);
        spinType=findViewById(R.id.spinnerType);
        pie=findViewById(R.id.pieChart);
        bar=findViewById(R.id.barchart);
        totalAmount=findViewById(R.id.amount);

        db=new KomunaluriDB(this,"Komunaluri.db",null,1);
        allList=db.getall();
    }
}
