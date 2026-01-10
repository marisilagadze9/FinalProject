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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        title=findViewById(R.id.title);
        add=findViewById(R.id.add);
        date=findViewById(R.id.date);

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


        ActivityResultLauncher<Intent> launcher=
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result->{
                            if (result.getResultCode()==RESULT_OK && result.getData()!=null) {
                                String item=result.getData().getStringExtra("item");
                                String amount=result.getData().getStringExtra("amount");
                                boolean paid=result.getData().getBooleanExtra("paid", false);
                                String d=result.getData().getStringExtra("date");

                                switch (item) {
                                    case "დენი":
                                        updatecard(deni, deniAmount, deniStatus, amount, paid,d);
                                        break;

                                    case "გაზი":
                                        updatecard(gazi, gasAmount, gasStatus, amount, paid,d);
                                        break;

                                    case "წყალი":
                                        updatecard(wyali, waterAmount, waterStatus, amount, paid,d);
                                        break;

                                    case "დასუფთავება":
                                        updatecard(dasuftaveba, cleaningAmount, cleaningStatus, amount, paid,d);
                                        break;

                                    case "ტელევიზია/ინტერნეტი":
                                        updatecard(tv, tvAmount, tvStatus, amount, paid,d);
                                        break;

                                    case "სხვა":
                                        updatecard(sxva, sxvaAmount, sxvaStatus, amount, paid,d);
                                        break;
                                }
                            }
                        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Add.class);
                launcher.launch(i);
            }
        });

    }
    void updatecard(CardView card,TextView newamount,TextView newstatus,String a,boolean p,String date){
        newamount.setText(a + "₾");
        if(p){
            newstatus.setText("გადახდილია - "+ date);
            card.setCardBackgroundColor(Color.parseColor("#E8F5E9"));
        }
        else{
            newstatus.setText("ვადა: " +date);
            card.setCardBackgroundColor(Color.parseColor("#FDECEA"));
        }
    }
}