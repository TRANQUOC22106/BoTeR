package com.example.boter.ui.User;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.boter.Adapter.Person;
import com.example.boter.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserFragment extends Fragment {

    private UserViewModel sendViewModel;
    private EditText edtName;
    private EditText edtTemp;
    private Button btnUpdate;
    private EditText edtUserId;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);
        sendViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        edtName = root.findViewById(R.id.editTextName);
        edtTemp = root.findViewById(R.id.editTextTemp);
        edtUserId = root.findViewById(R.id.editTextUserId);
        btnUpdate = root.findViewById(R.id.buttonUpdate);

        btnUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateUser();
                    }
                }
        );
        return root;
    }

    private void updateUser() {
        String name = edtName.getText().toString();
        int temp = Integer.parseInt(edtTemp.getText().toString());
        int userId = Integer.parseInt(edtUserId.getText().toString());
        Context context = getActivity();
        if (name.trim().isEmpty()){
            Toast.makeText(context, "名前を入力してください", Toast.LENGTH_SHORT).show();
            return;
        }
        CollectionReference userListRef = FirebaseFirestore.getInstance()
                .collection("UserList");
        userListRef.add(new Person(name, temp, userId));
        Toast.makeText(context,"提出された。", Toast.LENGTH_SHORT).show();
    }
}