package com.example.boter.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.boter.R;

public class Register extends AppCompatActivity {
    private EditText nameInput, phoneInput, emailInput, passwordInput,
            confirmPasswordInput;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


    }
}
