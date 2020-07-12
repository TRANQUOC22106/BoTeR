package com.example.boter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.boter.MainActivity;
import com.example.boter.R;
import com.example.boter.ui.login.ActivityLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Register extends AppCompatActivity{
    TextInputEditText mFullname, mPhone, mEmail, mPassword, mTemp, mStudentID;
    Button mRegisterBtn;
    FirebaseAuth fAuth;
    ImageButton mBackBtn;
    FirebaseFirestore fStore;
    ProgressBar progressBar;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullname = findViewById(R.id.name_edit_text);
        mPhone = findViewById(R.id.phone_edit_text);
        mEmail = findViewById(R.id.email_edit_text);
        mPassword = findViewById(R.id.password_edit_text);
        mBackBtn = findViewById(R.id.imageButtonBack);
        mRegisterBtn = findViewById(R.id.buttonRegisterA);
        mStudentID = findViewById(R.id.studentIdEdt);
        mTemp = findViewById(R.id.tempEditText);
        progressBar = findViewById(R.id.progressBarReg);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        mRegisterBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String email = Objects.requireNonNull(mEmail.getText()).toString().trim();
                        String password = Objects.requireNonNull(mPassword.getText()).toString().trim();
                        final String fullname = Objects.requireNonNull(mFullname.getText()).toString();
                        final String phone = Objects.requireNonNull(mPhone.getText()).toString();
                        final String studentID = Objects.requireNonNull(mStudentID.getText()).toString();
                        final String temp = Objects.requireNonNull(mTemp.getText()).toString();

                        if (TextUtils.isEmpty(email)){
                            mEmail.setError("Email is Required.");
                            return;
                        }
                        if (TextUtils.isEmpty(password)){
                            mPassword.setError("Password is Required.");
                            return;
                        }
                        if (password.length() < 6){
                            mPassword.setError("Password Must be >= 6 Charactor");
                            return;
                        }
                        progressBar.setVisibility(View.VISIBLE);
                        //register the user in firebase
                        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                                            //UserIDを自動設定する
                                            userID = fAuth.getCurrentUser().getUid();
                                            DocumentReference documentReference = fStore.collection("UserList").document(userID);
                                            Map<String,Object> user = new HashMap<>();
                                            user.put("fullname", fullname);
                                            user.put("email", email);
                                            user.put("phone", phone);
                                            user.put("studentID", studentID);
                                            user.put("temp", temp);

                                            documentReference.set(user).addOnSuccessListener(
                                                    new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("trident", "onSuccess: user Profile is created for" + userID);
                                                        }
                                                    }
                                            );
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        }else{
                                            Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                        );
                    }
                }
        );
        mBackBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                    }
                }
        );
    }
}
