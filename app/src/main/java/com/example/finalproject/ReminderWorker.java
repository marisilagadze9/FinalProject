package com.example.finalproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReminderWorker extends Worker {
    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context=getApplicationContext();
        boolean notifyEnabled=context.getSharedPreferences("settings",Context.MODE_PRIVATE).getBoolean("notify_enabled",false);
        boolean repeatEnabled=context.getSharedPreferences("settings",Context.MODE_PRIVATE).getBoolean("repeat_enabled",false);


        int dayBefore=context.getSharedPreferences("settings",Context.MODE_PRIVATE).getInt("notify_days",3);

        if(!notifyEnabled) return Result.success();

        KomunaluriDB db=new KomunaluriDB(context,"Komunaluri.db",null,1);
        List<Komunaluri> list=db.getall();

        SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        Date today=new Date();

        for(Komunaluri k : list){
            if(k.isPaid()) continue;

            try{
                Date dueDate=sdf.parse(k.getDate());
                if(dueDate==null) continue;
                long diffMillis=dueDate.getTime()-today.getTime();
                long diffDays=diffMillis/(1000*60*60*24);


                if (diffDays<0) continue;


                if (!repeatEnabled && diffDays!=dayBefore) continue;


                if (repeatEnabled && diffDays>dayBefore) continue;

                sendNotification(context, k);

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        return Result.success();
    }

    void sendNotification(Context context, Komunaluri k) {

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(
                    android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        NotificationManager nm=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId="reminder_channel";

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            NotificationChannel channel=new NotificationChannel(
                    channelId,
                    "კომუნალური შეტყობინებები",
                    NotificationManager.IMPORTANCE_HIGH
            );
            nm.createNotificationChannel(channel);
        }

        Notification notification=new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.sms)
                .setContentTitle("კომუნალური გადასახდელია")
                .setContentText(
                        k.getName() + " – " + k.getAmount() +
                                " ₾ | ბოლო ვადა: " + k.getDate()
                )
                .setAutoCancel(true)
                .build();

        nm.notify(k.getId(), notification);
    }



}
