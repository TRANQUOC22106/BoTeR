package com.example.boter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.boter.MainActivity;
import com.example.boter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity implements View.OnClickListener{
    TextInputEditText mFullname, mPhone, mEmail, mPassword;
    Button mRegisterBtn;
    FirebaseAuth fAuth;
    ImageButton mBackBtn;

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

        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        mRegisterBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Log.d("trident","fjhg");
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

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
        //register the user in firebase
        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else{
                            Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}
