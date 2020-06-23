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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MyAdapter extends FirestoreRecyclerAdapter<Person, MyAdapter.PersonHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyAdapter(@NonNull FirestoreRecyclerOptions<Person> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final PersonHolder holder, int position, @NonNull Person model) {
        final String idUser = getSnapshots().getSnapshot(holder.getAdapterPosition()).getId();
        holder.name.setText(model.getName());
        holder.userId.setText(String.valueOf(model.getUserId()));
        holder.temp.setText(String.valueOf(model.getTemp()));
        holder.imageButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentDetail = new Intent(holder.itemView.getContext(), DetailActivity.class);
                        intentDetail.putExtra("userId", idUser);
                        holder.itemView.getContext().startActivity(intentDetail);
                    }
                }
        );
    }

    @NonNull
    @Override
    public PersonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_bodytempurate,
                parent, false);
        return new PersonHolder(v);
    }

    public class PersonHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView temp;
        TextView userId;
        ImageButton imageButton;
        public PersonHolder(@NonNull View itemView) {
            super(itemView);

            imageButton = itemView.findViewById(R.id.imageButtonNext);
            name = itemView.findViewById(R.id.name);
            temp = itemView.findViewById(R.id.temp);
            userId = itemView.findViewById(R.id.textViewUserId);
        }
    }
}
