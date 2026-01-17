package com.example.finalproject;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Settings extends AppCompatActivity {

    Switch swNotify, swRepeat;
    NumberPicker npDays;
    TimePicker tpTime;
    ImageButton save, back;
    SharedPreferences prefs;

    private final int NOTIF_PERMISSION_CODE = 102;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        swNotify=findViewById(R.id.swNotify);
        swRepeat=findViewById(R.id.swRepeat);
        npDays=findViewById(R.id.numberPickerDays);
        tpTime=findViewById(R.id.timePickerNotify);
        save=findViewById(R.id.save);
        back=findViewById(R.id.backbtn);

        prefs=getSharedPreferences("settings", Context.MODE_PRIVATE);


        npDays.setMinValue(1);
        npDays.setMaxValue(30);
        npDays.setWrapSelectorWheel(true);
        npDays.setEnabled(false);


        tpTime.setIs24HourView(true);
        tpTime.setEnabled(false);


        swNotify.setChecked(prefs.getBoolean("notify_enabled", false));
        swRepeat.setChecked(prefs.getBoolean("repeat_enabled", false));
        npDays.setValue(prefs.getInt("notify_days", 3));
        tpTime.setHour(prefs.getInt("notify_hour", 9));
        tpTime.setMinute(prefs.getInt("notify_minute", 0));

        boolean notifyEnabled=swNotify.isChecked();
        swRepeat.setEnabled(notifyEnabled);
        npDays.setEnabled(notifyEnabled);
        tpTime.setEnabled(notifyEnabled);

        swNotify.setOnCheckedChangeListener((buttonView, isChecked) -> {
            swRepeat.setEnabled(isChecked);
            npDays.setEnabled(isChecked);
            tpTime.setEnabled(isChecked);
        });

        back.setOnClickListener(v -> finish());

        save.setOnClickListener(v -> {
            SharedPreferences.Editor editor=prefs.edit();
            editor.putBoolean("notify_enabled", swNotify.isChecked());
            editor.putBoolean("repeat_enabled", swRepeat.isChecked());
            editor.putInt("notify_days", npDays.getValue());
            editor.putInt("notify_hour", tpTime.getHour());
            editor.putInt("notify_minute", tpTime.getMinute());
            editor.apply();


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},
                            NOTIF_PERMISSION_CODE);

                    return;
                }
            }

            scheduleNotification();

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIF_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scheduleNotification();
                Toast.makeText(this, "შეტყობინებები ჩართულია", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "შეტყობინებები არ არის ნებადართული", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void scheduleNotification() {
        boolean notifyEnabled=prefs.getBoolean("notify_enabled", false);
        if (!notifyEnabled) return;

        int hour=prefs.getInt("notify_hour", 9);
        int minute=prefs.getInt("notify_minute", 0);

        Calendar now=Calendar.getInstance();
        Calendar notifyTime=Calendar.getInstance();
        notifyTime.set(Calendar.HOUR_OF_DAY, hour);
        notifyTime.set(Calendar.MINUTE, minute);
        notifyTime.set(Calendar.SECOND, 0);
        notifyTime.set(Calendar.MILLISECOND, 0);

        long delay=notifyTime.getTimeInMillis() - now.getTimeInMillis();
        if (delay < 0) {

            delay += TimeUnit.DAYS.toMillis(1);
        }


        if (delay > TimeUnit.DAYS.toMillis(1)) {
            delay = TimeUnit.DAYS.toMillis(1);
        }

        try {
            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .build();

            WorkManager.getInstance(this).enqueueUniqueWork(
                    "reminder_one_time",
                    ExistingWorkPolicy.REPLACE,
                    request
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
