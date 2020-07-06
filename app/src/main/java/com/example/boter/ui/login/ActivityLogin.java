package com.example.boter.ui.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.boter.Activity.Register;
import com.example.boter.MainActivity;
import com.example.boter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    Button btnRegister, btnForgotPassword, btnLogin;
    FirebaseAuth fAuth;
    EditText mEmail, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.edtMail);
        mPassword = findViewById(R.id.edtPassword);
        btnForgotPassword = findViewById(R.id.buttonForgotPassword);
        btnRegister = findViewById(R.id.buttonRegister);
        btnLogin = findViewById(R.id.buttonLogin);

        btnLogin.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        fAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegister:
                register();
                break;
            case R.id.buttonForgotPassword:
                forgotPassword(v);
            case R.id.buttonLogin:
                loginMail();
        }

    }
    private void loginMail() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Email is Required.");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Password is Required.");
            return;
        }
        if (password.length() < 6) {
            mPassword.setError("Password Must be >= 6 Charactor");
            return;
        }
        //Login with firebase
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ActivityLogin.this, "Logged in SuccessFully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(ActivityLogin.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
    private View forgotPassword(View v) {
        final EditText resetEmail = new EditText(v.getContext());
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
        passwordResetDialog.setTitle("Reset Password ?");
        passwordResetDialog.setMessage("Enter your Email to received link.");
        passwordResetDialog.setView(resetEmail);

        passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //extract the email and send reset link
                String mail = resetEmail.getText().toString();
                fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ActivityLogin.this, "Reset link sent to your Email", Toast.LENGTH_SHORT).show();
                            }
                        }
                ).addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ActivityLogin.this, "Error ! Reset link is not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
        passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //close the dialog
            }
        });
        passwordResetDialog.create().show();
        return v;
    }
    private void register() {
        Intent intent = new Intent(ActivityLogin.this, Register.class);
        startActivity(intent);
    }
}
