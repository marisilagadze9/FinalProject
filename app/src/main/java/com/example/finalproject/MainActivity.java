package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
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


        ActivityResultLauncher<Intent> launcher=
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result->{
                            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                String item = result.getData().getStringExtra("item");
                                String amount = result.getData().getStringExtra("amount");
                                boolean paid = result.getData().getBooleanExtra("paid", false);


                                switch (item) {
                                    case "დენი":
                                        deniAmount.setText(amount + " ₾");
                                        deniStatus.setText(paid ? "გადახდილია ✅" : "გადაუხდელია ❌");
                                        break;
                                    case "გაზი":
                                        gasAmount.setText(amount + " ₾");
                                        gasStatus.setText(paid ? "გადახდილია ✅" : "გადაუხდელია ❌");
                                        break;
                                    case "წყალი":
                                        waterAmount.setText(amount + " ₾");
                                        waterStatus.setText(paid ? "გადახდილია ✅" : "გადაუხდელია ❌");
                                        break;
                                    case "დასუფთავება":
                                        cleaningAmount.setText(amount + " ₾");
                                        cleaningStatus.setText(paid ? "გადახდილია ✅" : "გადაუხდელია ❌");
                                        break;
                                    case "ტელევიზია/ინტერნეტი":
                                        tvAmount.setText(amount + " ₾");
                                        tvStatus.setText(paid ? "გადახდილია ✅" : "გადაუხდელია ❌");
                                        break;

                                    case "სხვა":
                                        sxvaAmount.setText(amount + " ₾");
                                        sxvaStatus.setText(paid ? "გადახდილია ✅" : "გადაუხდელია ❌");
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
}