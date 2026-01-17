package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Navigation extends Fragment {
    LinearLayout home,statistic,history,settings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
         View v=inflater.inflate(R.layout.bottom,container,false);

         home=v.findViewById(R.id.homebtn);
         statistic=v.findViewById(R.id.statisticbtn);
         history=v.findViewById(R.id.historybtn);
         settings=v.findViewById(R.id.settingscbtn);

         home.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getActivity(), MainActivity.class));
             }
         });

         history.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getActivity(), History.class));
             }
         });

         statistic.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getActivity(), Statistic.class));
             }
         });

         settings.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getActivity(), Settings.class));
             }
         });

         return v;



    }
}
