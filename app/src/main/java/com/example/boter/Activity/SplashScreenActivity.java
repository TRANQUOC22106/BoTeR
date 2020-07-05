package com.example.boter.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.boter.MainActivity;
import com.example.boter.ui.login.ActivityLogin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = fAuth.getCurrentUser();
        //ここでLoginをしたかどうか確認する。していない場合、ActivityLogin.javaに移す。
//Loginをしている場合、MainActivity.classに移す
        Intent intent = new Intent(this, currentUser == null
                ? ActivityLogin.class : MainActivity.class);
        startActivity(intent);
        finish();
    }
}
