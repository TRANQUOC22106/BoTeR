package com.example.boter.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.boter.MainActivity;
import com.example.boter.ui.login.ActivityLogin;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean a = true;
        //ここでLoginをしたかどうか確認する。していない場合、ActivityLogin.javaに移す。
//Loginをしている場合、MainActivity.classに移す
        Intent intent = new Intent(this, a == true
                ? ActivityLogin.class : MainActivity.class);
        startActivity(intent);
        finish();
    }
}
