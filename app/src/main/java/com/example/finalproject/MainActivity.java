package com.example.finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView title,date;
    ImageButton add;
    ScrollView scrol;

    TextView deniAmount, deniStatus;
    TextView gasAmount, gasStatus;
    TextView waterAmount, waterStatus;
    TextView cleaningAmount, cleaningStatus;
    TextView tvAmount, tvStatus;
    TextView sxvaAmount, sxvaStatus;
    CardView deni,wyali,gazi,dasuftaveba,tv,sxva;
    KomunaluriDB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        title=findViewById(R.id.title);
        add=findViewById(R.id.add);
        date=findViewById(R.id.date);

        SimpleDateFormat sdf=new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        String today=sdf.format(new Date());
        date.setText(today);


        db=new KomunaluriDB(this,"Komunaluri.db",null,1);

        scrol=findViewById(R.id.home);

        deniAmount=findViewById(R.id.deniamount);
        deniStatus=findViewById(R.id.denistatus);

        gasAmount=findViewById(R.id.gaziamount);
        gasStatus=findViewById(R.id.gazistatus);

        waterAmount=findViewById(R.id.wyaliamount);
        waterStatus=findViewById(R.id.wyalistatus);

        cleaningAmount=findViewById(R.id.dasuftavebaamount);
        cleaningStatus=findViewById(R.id.dasuftavebastatus);

        tvAmount=findViewById(R.id.telamount);
        tvStatus=findViewById(R.id.telstatus);

        sxvaAmount=findViewById(R.id.sxvaamount);
        sxvaStatus=findViewById(R.id.sxvastatus);

        deni=findViewById(R.id.denicard);
        wyali=findViewById(R.id.wyalicard);
        gazi=findViewById(R.id.gazicard);
        dasuftaveba=findViewById(R.id.cleaningcard);
        tv=findViewById(R.id.intcard);
        sxva=findViewById(R.id.sxvacard);

        getSupportFragmentManager().beginTransaction()
                        .replace(R.id.bottom,new Navigation()).commit();



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Add.class);
                startActivity(i);
            }
        });
        loadData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    void loadData(){
        List<Komunaluri> list=db.getall();
        for(Komunaluri k : list){
            switch(k.getName()){
                case "დენი":
                    updatecard(deni,deniAmount,deniStatus,k.getAmount(),k.isPaid(),k.getDate(),k);
                    break;
                case "გაზი":
                    updatecard(gazi, gasAmount, gasStatus, k.getAmount(), k.isPaid(), k.getDate(),k);
                    break;
                case "წყალი":
                    updatecard(wyali, waterAmount, waterStatus, k.getAmount(), k.isPaid(), k.getDate(),k);
                    break;
                case "დასუფთავება":
                    updatecard(dasuftaveba, cleaningAmount, cleaningStatus, k.getAmount(), k.isPaid(), k.getDate(),k);
                    break;
                case "ტელევიზია/ინტერნეტი":
                    updatecard(tv, tvAmount, tvStatus, k.getAmount(), k.isPaid(), k.getDate(),k);
                    break;
                case "სხვა":
                    updatecard(sxva, sxvaAmount, sxvaStatus, k.getAmount(), k.isPaid(), k.getDate(),k);
                    break;



            }
        }

    }

    void updatecard(CardView card,TextView newamount,TextView newstatus,double a,boolean p,String date,Komunaluri k){
        newamount.setText(a + "₾");
        if(p){
            newstatus.setText("გადახდილია - "+ date);
            card.setCardBackgroundColor(Color.parseColor("#E8F5E9"));
        }
        else{
            newstatus.setText("ვადა: " +date);
            card.setCardBackgroundColor(Color.parseColor("#FDECEA"));
        }
        newstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String today=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());


                newstatus.setText("გადახდილია - " + today);


                card.setCardBackgroundColor(Color.parseColor("#E8F5E9"));


                k.isPaid(true);
                k.setDate(today);


                db.update(k);
            }
        });
    }
}