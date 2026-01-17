package com.example.finalproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private final int SMS_PERMISSION_CODE=101;


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


        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                ReminderWorker.class,
                1, TimeUnit.DAYS
        ).build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "reminder",
                ExistingPeriodicWorkPolicy.REPLACE,
                request
        );

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        MainActivity.this,
                        Manifest.permission.READ_SMS
                ) == PackageManager.PERMISSION_GRANTED) {

                    readSMS();
                }

                loadData();
            }
        });


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS},SMS_PERMISSION_CODE);
        }
        else{
            readSMS();
        }
        loadData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)==PackageManager.PERMISSION_GRANTED){
            readSMS();
        }
        loadData();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==SMS_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                readSMS();
                loadData();
            }
        }
    }


    void readSMS(){
        Uri uri=Uri.parse("content://sms/inbox");
        Cursor cur=getContentResolver().query(uri,null,null,null,"date DESC");

        if(cur!=null){
            while(cur.moveToNext()){
                String body=cur.getString(cur.getColumnIndexOrThrow("body"));
                String address=cur.getString(cur.getColumnIndexOrThrow("address"));

                processSMS(body,address);
            }
            cur.close();
        }

    }
    void processSMS(String body,String address){


        String service=getServiceFromBody(body);
        if(service.equals("სხვა")) return;

        double amount=extractAmount(body);
        String date=extractDate(body);
        Pattern payPattern=Pattern.compile("chairicxa|charicx|dafarulia", Pattern.CASE_INSENSITIVE);
        Matcher m=payPattern.matcher(body);
        boolean isPayment=m.find();

        Log.d("PAYMENT_DEBUG", "SMS Body: " + body);
        Log.d("PAYMENT_DEBUG", "Detected as payment: " + isPayment);

        if (date==null || date.isEmpty()) {
            date=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        }

        if(isPayment){
            Komunaluri k=db.getByName(service);
            if(k!=null){
                k.isPaid(true);
                k.setDate(date);
                db.update(k);
            }
            else{
                db.add(new Komunaluri(service,amount,true,date));
            }
            loadData();
        }
        else{
            boolean exists=false;
            List<Komunaluri> list=db.getall();
            for(Komunaluri k : list){
                if (k.getName().equals(service) &&
                        k.getAmount()==amount &&
                        k.getDate().equals(date) &&
                        !k.isPaid()) {
                    exists=true;
                    break;
                }
            }
            if(!exists){
                db.add(new Komunaluri(service, amount, false, date));
                loadData();
            }
        }


    }

    double extractAmount(String body){
        Pattern p = Pattern.compile("(\\d+[\\.,]?\\d*)\\s*(lari)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(body);
        if (m.find()) {
            return Double.parseDouble(m.group(1).replace(",", "."));
        }
        return 0;
    }



    String extractDate(String body){
        Pattern p = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4})");
        Matcher m = p.matcher(body);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }


    String getServiceFromBody(String body) {
        body = body.toLowerCase();

        if (body.contains("silknet") || body.contains("სილქნეტ"))
            return "ტელევიზია/ინტერნეტი";

        if (body.contains("energo") || body.contains("ენერგო"))
            return "დენი";

        if (body.contains("socar") || body.contains("გაზ"))
            return "გაზი";

        if (body.contains("წყალ") || body.contains("wyali"))
            return "წყალი";

        if (body.contains("dasuftaveba"))
            return "დასუფთავება";

        return "სხვა";
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


    void updatecard(CardView card,TextView newamount,TextView newstatus,double a,boolean p,String date,Komunaluri k) {
        newamount.setText(a + "₾");
        if(p){
            newstatus.setText("გადახდილია - "+ date);
            card.setCardBackgroundColor(Color.parseColor("#E8F5E9"));
        } else {
            newstatus.setText("ვადა: " + date);
            card.setCardBackgroundColor(Color.parseColor("#FDECEA"));
        }


    }
}