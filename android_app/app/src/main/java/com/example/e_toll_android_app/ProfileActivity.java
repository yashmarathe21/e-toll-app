package com.example.e_toll_android_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    Button buttonAddVehicle;
    Button buttonAddMoney;
    Button buttonVehicle;
    Button buttonMoney;
    Button buttonImage;

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            default:
                return;
            case R.id.buttonAddMoneyNew:
                startActivity(new Intent((Context)this, CheckMoney.class));
                return;
            case R.id.buttonImage:
                startActivity(new Intent((Context)this, VehicleImage.class));
                return;
            case R.id.buttonAddVehicle:
                startActivity(new Intent((Context)this, AddVehicle.class));
                return;
            case R.id.buttonAddMoney:
                startActivity(new Intent((Context)this, AddMoney.class));
        }
    }
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_profile);
        this.mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.buttonAddVehicle).setOnClickListener(this);
        findViewById(R.id.buttonAddMoneyNew).setOnClickListener(this);
        findViewById(R.id.buttonMoney).setOnClickListener(this);
        findViewById(R.id.buttonImage).setOnClickListener(this);
    }
}
