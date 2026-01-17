package com.example.finalproject;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        spinMonth.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,months));
        spinType.setAdapter((new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,types)));

        AdapterView.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateStats();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinMonth.setOnItemSelectedListener(listener);
        spinType.setOnItemSelectedListener(listener);
    }
    void updateStats(){
        allList=db.getall();
        String selectedMonth=spinMonth.getSelectedItem().toString();
        String selectedType=spinType.getSelectedItem().toString();

        List<Komunaluri> filtered=new ArrayList<>();

        for(Komunaluri k : allList){
            boolean okm=true;
            boolean okt=true;

            if(!selectedMonth.equals("ყველა")){
                String[] parts=k.getDate().contains("/") ?
                        k.getDate().split("/") :
                        k.getDate().split("\\.");
                int monthNum=Integer.parseInt(parts[1].replaceFirst("^0+",""));
                okm=monthStringToIndex(selectedMonth)==monthNum;
            }

            if(!selectedType.equals("ყველა")){
                okt=k.getName().equals(selectedType);
            }

            if(okm && okt) filtered.add(k);
        }

        double total=0;
        for(Komunaluri K : filtered){
            total+=K.getAmount();
        }
        totalAmount.setText(total+ " ₾");

        if (filtered.isEmpty()) {
            pie.clear();
            bar.clear();
            totalAmount.setText("0 ₾");
            return;
        }

        Map<String, Float> sums = new HashMap<>();

        for (Komunaluri k : filtered) {
            float current = sums.containsKey(k.getName())
                    ? sums.get(k.getName())
                    : 0f;
            sums.put(k.getName(), current + (float) k.getAmount());
        }

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        for (String key : sums.keySet()) {
            pieEntries.add(new PieEntry(sums.get(key), key));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "კომუნალური");

        pieDataSet.setColors(new int[]{
                Color.parseColor("#1E88E5"),
                Color.parseColor("#43A047"),
                Color.parseColor("#F4511E"),
                Color.parseColor("#8E24AA"),
                Color.parseColor("#FDD835"),
                Color.parseColor("#00ACC1"),
                Color.parseColor("#6D4C41")
        });

        PieData pieData=new PieData(pieDataSet);
        pie.setData(pieData);
        pie.setUsePercentValues(true);
        pie.getDescription().setEnabled(false);
        pie.invalidate();


        ArrayList<BarEntry> barEntries=new ArrayList<>();
        for(int i=0; i<filtered.size(); i++){
            barEntries.add(new BarEntry(i,(float) filtered.get(i).getAmount()));
        }

        BarDataSet barDataSet=new BarDataSet(barEntries,"თანხა");
        barDataSet.setColors(Color.parseColor("#1E88E5"));

        BarData barData=new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        bar.setData(barData);
        bar.getDescription().setEnabled(false);
        bar.invalidate();

    }

    int monthStringToIndex(String monthName) {
        switch (monthName) {
            case "იანვარი": return 1;
            case "თებერვალი": return 2;
            case "მარტი": return 3;
            case "აპრილი": return 4;
            case "მაისი": return 5;
            case "ივნისი": return 6;
            case "ივლისი": return 7;
            case "აგვისტო": return 8;
            case "სექტემბერი": return 9;
            case "ოქტომბერი": return 10;
            case "ნოემბერი": return 11;
            case "დეკემბერი": return 12;
            default: return 0;
        }
    }
}
