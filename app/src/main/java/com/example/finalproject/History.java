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

        for(Komunaluri k : allList){
            boolean okm=true;
            boolean okt=true;

            if(!monthsel.equals("ყველა")){
                String[] parts=k.getDate().split("/");
                int m=Integer.parseInt(parts[1])-1;
                okm=months[m+1].equals(monthsel);
            }

            if(!typesel.equals(("ყველა"))){
                okt=k.getName().equals(typesel);

            }

            if(okm && okt){
                filteres.add(k);
            }
        }
        adapter.updateList(filteres);
    }

}
