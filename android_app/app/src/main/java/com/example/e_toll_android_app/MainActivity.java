package com.example.e_toll_android_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextEmail;
    TextView textViewSignUp;
    Button buttonLogin;
    EditText editTextPassword;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    private void userLogin() {
        String str1 = this.editTextEmail.getText().toString().trim();
        String str2 = this.editTextPassword.getText().toString().trim();
        if (str1.isEmpty()) {
            this.editTextEmail.setError("Email is required");
            this.editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(str1).matches()) {
            this.editTextEmail.setError("Please enter a valid email");
            this.editTextEmail.requestFocus();
            return;
        }
        if (str2.isEmpty()) {
            this.editTextPassword.setError("Password is required");
            this.editTextPassword.requestFocus();
            return;
        }
        if (str2.length() < 6) {
            this.editTextPassword.setError("Minimum length of password should be 6");
            this.editTextPassword.requestFocus();
            return;
        }
        this.progressBar.setVisibility(View.VISIBLE);
        this.mAuth.signInWithEmailAndPassword(str1, str2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            public void onComplete(Task<AuthResult> param1Task) {
                Intent intent;
                if (param1Task.isSuccessful()) {
                    intent = new Intent((Context)MainActivity.this, ProfileActivity.class);
                    MainActivity.this.startActivity(intent);
                    return;
                }
                // Toast.makeText(MainActivity.this.getApplicationContext(), intent.getException().getMessage(), 0).show();
            }
        });
    }

    public void onClick(View paramView) {
        int i = paramView.getId();
        if (i != R.id.buttonLogin) {
            if (i != R.id.textViewSignUp)
                return;
            startActivity(new Intent((Context)this, SignUpActivity.class));
            return;
        }
        userLogin();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);
        this.editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        this.editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        this.progressBar = (ProgressBar)findViewById(R.id.progressBar);
        this.mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.textViewSignUp).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
    }
}
