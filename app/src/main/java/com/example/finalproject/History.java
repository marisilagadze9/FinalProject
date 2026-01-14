package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    RecyclerView recycler;
    ImageButton back;
    Spinner month,type;
    HistoryAdapter adapter;
    List<Komunaluri> allList=new ArrayList<>();
    KomunaluriDB db;

    String[] months = {"ყველა","იანვარი","თებერვალი","მარტი","აპრილი","მაისი",
            "ივნისი","ივლისი","აგვისტო","სექტემბერი","ოქტომბერი","ნოემბერი","დეკემბერი"};

    String[] types = {"ყველა","დენი","გაზი","წყალი","დასუფთავება","ტელევიზია/ინტერნეტი","სხვა"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.history);
        recycler=findViewById(R.id.recycleHistory);
        month=findViewById(R.id.spinnerMonth);
        type=findViewById(R.id.spinnerType);
        back=findViewById(R.id.back);
         back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });
        db=new KomunaluriDB(this,"Komunaluri.db",null,1);
        allList=db.getall();

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter=new HistoryAdapter(this, allList);
        recycler.setAdapter(adapter);

        month.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, months));

        type.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, types));

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        };

        month.setOnItemSelectedListener(listener);
        type.setOnItemSelectedListener(listener);
    }

    void filter(){
        String monthsel=month.getSelectedItem().toString();
        String typesel=type.getSelectedItem().toString();

        List<Komunaluri> filteres=new ArrayList<>();

        int selectedMonthNumber = monthStringToIndex(monthsel);

        for (Komunaluri k : allList) {
            boolean okm = true;
            boolean okt = true;


            String[] parts;
            if (k.getDate().contains("/")) {
                parts = k.getDate().split("/");
            } else {
                parts = k.getDate().split("\\.");
            }

            int monthNumber = Integer.parseInt(parts[1].replaceFirst("^0+", ""));

            if (!monthsel.equals("ყველა")) {
                okm = monthNumber == selectedMonthNumber;
            }

            if (!typesel.equals("ყველა")) {
                okt = k.getName().equals(typesel);
            }

            if(okm && okt){
                filteres.add(k);
            }
        }
        adapter.updateList(filteres);
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
            default: return 0; // "ყველა"
        }
    }
}
