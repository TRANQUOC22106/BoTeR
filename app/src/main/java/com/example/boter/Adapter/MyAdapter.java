package com.example.boter.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boter.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.DataViewHolder> {

    private List<Person> people;

    public MyAdapter(List<Person> people) {
        this.people = people;
    }
    //img_thermometer.png
    @NonNull
    @Override
    public MyAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_bodytempurate, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.DataViewHolder holder, int position) {

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDetail = new Intent(holder.itemView.getContext(), DetailActivity.class);
                holder.itemView.getContext().startActivity(intentDetail);
            }
        });
        String name = people.get(position).getName();
        holder.tvName.setText(name);
        String nhietDo = people.get(position).getNhietDo();
        holder.nhietDo.setText(nhietDo);
    }

    @Override
    public int getItemCount() {
        return people == null ? 0 : people.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder{

//        TextView date;
//        TextView name;
        TextView nhietDo;
        ImageButton imageButton;
            TextView tvName;
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
//            date = (TextView) itemView.findViewById(R.id.date);
            nhietDo = (TextView) itemView.findViewById(R.id.number);
            imageButton = (ImageButton) itemView.findViewById(R.id.imageButton3);
            tvName = (TextView) itemView.findViewById(R.id.name);

        }
    }
}
