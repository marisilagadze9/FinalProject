package com.example.finalproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Add extends AppCompatActivity {
    Spinner item;
    EditText amount;
    CheckBox paid;
    Button save;
    ImageButton back;
    Button selectdate;
    TextView textdate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addlayout);
        item=findViewById(R.id.item);
        amount=findViewById(R.id.amount);
        paid=findViewById(R.id.paid);

        save=findViewById(R.id.save);
        back=findViewById(R.id.back);
        selectdate=findViewById(R.id.selectdate);
        textdate=findViewById(R.id.datetext);
        final Calendar calendar=Calendar.getInstance();

        String[] items={
                "დენი","გაზი","წყალი","დასუფთავება","ტელევიზია/ინტერნეტი","სხვა"
        };

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,items);
        item.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd=new DatePickerDialog(Add.this,
                        (view, year1, month1, dayOfMonth) -> {
                            calendar.set(year1, month1, dayOfMonth);
                            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            textdate.setText(sdf.format(calendar.getTime()));
                        }, year, month, day);
                dpd.show();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i=item.getSelectedItem().toString();
                String a=amount.getText().toString();
                boolean p=paid.isChecked();
                String date=textdate.getText().toString();

                Intent data=new Intent();
                data.putExtra("item",i);
                data.putExtra("amount",a);
                data.putExtra("paid",p);
                data.putExtra("date",date);
                setResult(RESULT_OK,data);
                finish();
            }
        });


    }
}
