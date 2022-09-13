package com.example.safeup.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safeup.DetailsActivity;
import com.example.safeup.HolidaysRepo;
import com.example.safeup.models.HolidayModelItem;
import com.example.safeup.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class HolidayEntryAdapter extends RecyclerView.Adapter<HolidayEntryAdapter.ViewHolder> {

    private ArrayList<HolidayModelItem> holidaysList;
    private Context context;
    public HolidayEntryAdapter( Context context){
        this.context = context;
        holidaysList = new ArrayList<>();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tmp = LayoutInflater.from(parent.getContext()).inflate(R.layout.holiday_entry,parent,false);
        return new ViewHolder(tmp);
    }

    public void setList(ArrayList<HolidayModelItem> list){

        this.holidaysList = list;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HolidayModelItem curr = holidaysList.get(position);
        holder.name.setText(curr.getName());
        holder.date.setText(curr.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                Gson gson = new Gson();
                String json = gson.toJson(curr);
                intent.putExtra("selected_holiday",json);
                context.startActivity(intent);
            }
        });
        ArrayList<HolidayModelItem> fav = HolidaysRepo.getInstance().getFavHolidays().getValue();
        if(fav!= null && fav.contains(curr)){
            holder.holiday_fav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
        }else {
            holder.holiday_fav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_non_fav));
        }
        holder.holiday_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<HolidayModelItem> fav = HolidaysRepo.getInstance().getFavHolidays().getValue();
                ArrayList<HolidayModelItem> x;
                if(fav!= null && fav.contains(curr)){
                    holder.holiday_fav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_non_fav));
                    HolidaysRepo.getInstance().removeFavHoliday(curr);
                }else {
                    holder.holiday_fav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
                    HolidaysRepo.getInstance().appendFavHoliday(curr);
                }

                notifyDataSetChanged();

            }

        });
    }

    @Override
    public int getItemCount() {
        if(holidaysList!=null) {
            return holidaysList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView date;
        ImageView holiday_fav;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.holiday_name);
            date = itemView.findViewById(R.id.holiday_date);
            holiday_fav = itemView.findViewById(R.id.holiday_fav);
        }


    }
}
