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

    KomunaluriDB db;
    final Calendar calendar=Calendar.getInstance();

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
        db=new KomunaluriDB(this,"Komunaluri.db",null,1);


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
                if (amount.getText().toString().isEmpty()) {
                    amount.setError("შეიყვანე თანხა");
                    return;
                }

                String name=item.getSelectedItem().toString();
                double a=Double.parseDouble(amount.getText().toString());
                boolean ispaid=paid.isChecked();

                String date;
                if (ispaid) {
                    SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    date=sdf.format(Calendar.getInstance().getTime());
                } else {
                    if (textdate.getText().toString().isEmpty()) {
                        textdate.setError("აირჩიე გადახდის ვადა");
                        return;
                    }
                    date=textdate.getText().toString();
                }

                db.add(new Komunaluri(name, a, ispaid, date));
                finish();
            }
        });


    }
}
