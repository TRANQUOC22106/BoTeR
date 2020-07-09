package com.example.boter.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boter.Adapter.MyAdapter;
import com.example.boter.Adapter.Person;
import com.example.boter.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment{

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference listUser = db.collection("UserList");
    private MyAdapter adapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recycler_view);
//        サイズを設定
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        showAllUserList();
        return root;
    }

    //if(studentID != 0){
    // finish()
    // }
    private void showAllUserList() {
        Query queryShowAll = listUser.orderBy("temp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Person> options = new FirestoreRecyclerOptions.Builder<Person>()
                .setQuery(queryShowAll, Person.class)
                .build();

        adapter = new MyAdapter(options);
//        recyclerView.removeAllViews();
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onStart() {
        adapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop() {
        adapter.stopListening();
        super.onStop();
    }
}