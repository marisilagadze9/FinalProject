package com.example.finalproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Holder> {
    Context context;
    List<Komunaluri> list;

    public HistoryAdapter(Context context, List<Komunaluri> list) {
        this.context=context;
        this.list=list;
    }

    public void updateList(List<Komunaluri> newList){
        list=newList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Komunaluri k=list.get(position);

        holder.name.setText(k.getName());
        holder.amount.setText("თანხა: " + k.getAmount() + " ₾");

        if(k.isPaid()){
            holder.date.setText("გადახდილია: " + k.getDate());
            holder.card.setCardBackgroundColor(Color.parseColor("#E8F5E9"));
        }else{
            holder.date.setText("ვადა: " + k.getDate());
            holder.card.setCardBackgroundColor(Color.parseColor("#FDECEA"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView name, amount, date;
        CardView card;


        public Holder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.Name);
            amount=itemView.findViewById(R.id.amount);
            date=itemView.findViewById(R.id.date);
            card=(CardView) itemView;

        }
    }
}
