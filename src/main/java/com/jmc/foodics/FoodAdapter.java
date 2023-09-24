package com.jmc.foodics;


import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import com.bumptech.glide.Glide;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder>{

    Context context;
    ArrayList<FoodList> foodList;

    public FoodAdapter(Context context, ArrayList<FoodList> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.food_adapter,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.MyViewHolder holder, int position) {
        FoodList food  = foodList.get(position);

        Glide.with(holder.itemView)
                .load(food.getUrl())
                .into(holder.iv);
        holder.tv.setText(food.getFoodName());

        holder.tView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, Order.class);

                String textViewText = holder.tv.getText().toString();
                intent.putExtra("textViewText", textViewText);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tv,tView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.image);
            tv = itemView.findViewById(R.id.name);
            tView = itemView.findViewById(R.id.View);
        }
    }
}