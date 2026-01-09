package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Add extends AppCompatActivity {
    Spinner item;
    EditText amount;
    CheckBox paid;
    Button save;
    ImageButton back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addlayout);
        item=findViewById(R.id.item);
        amount=findViewById(R.id.amount);
        paid=findViewById(R.id.paid);

        save=findViewById(R.id.save);
        back=findViewById(R.id.back);

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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i=item.getSelectedItem().toString();
                String a=amount.getText().toString();
                boolean p=paid.isChecked();

                Intent data=new Intent();
                data.putExtra("item",i);
                data.putExtra("amount",a);
                data.putExtra("paid",p);
                setResult(RESULT_OK,data);
                finish();
            }
        });


    }
}
