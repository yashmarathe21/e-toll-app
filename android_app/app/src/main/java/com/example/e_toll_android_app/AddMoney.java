package com.example.e_toll_android_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddMoney extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference databaseMoney;

    EditText editTextAddMoney;

    EditText editTextLicenseNo;

    ProgressBar progressBar;

    private void addMoney() {
        String str1 = this.editTextLicenseNo.getText().toString().trim();
        if (str1.isEmpty()) {
            this.editTextLicenseNo.setError("License Number is required");
            this.editTextLicenseNo.requestFocus();
            return;
        }
        String str2 = this.editTextAddMoney.getText().toString();
        if (str2.isEmpty()) {
            this.editTextAddMoney.setError("Enter amount to be added");
            this.editTextAddMoney.requestFocus();
            return;
        }
        try {
            int i = Integer.parseInt(str2);
            addMoneyToFirebase(str1, Integer.valueOf(i));
            return;
        } catch (NumberFormatException numberFormatException) {
            this.editTextAddMoney.setError("Integer input is required");
            this.editTextAddMoney.requestFocus();
            return;
        }
    }

    private void addMoneyToFirebase(final String licenseNo, final Integer moneyInt) {
        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        databaseMoney.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError param1DatabaseError) {}

            public void onDataChange(DataSnapshot param1DataSnapshot) {
                if (param1DataSnapshot.child("vehicles").child(licenseNo).exists()) {
                    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
                    hashMap.put("money", moneyInt);
                    RootRef.child("vehicles").child(licenseNo).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(Task<Void> param2Task) {
                            if (param2Task.isSuccessful()) {
                                Toast.makeText((Context)AddMoney.this, "Your money has been added", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent((Context)AddMoney.this, ProfileActivity.class);
                                AddMoney.this.startActivity(intent);
                                return;
                            }
                            Toast.makeText((Context)AddMoney.this, "Network Error: Please try again after some time...", Toast.LENGTH_LONG).show();
                        }
                    });
                    return;
                }
                AddMoney addMoney = AddMoney.this;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("This ");
                stringBuilder.append(licenseNo);
                stringBuilder.append(" does not already exists.");
                Toast.makeText((Context)addMoney, stringBuilder.toString(), 0).show();
                Toast.makeText((Context)AddMoney.this, "Please enter your correct license no", 0).show();
                Intent intent = new Intent((Context)AddMoney.this, ProfileActivity.class);
                AddMoney.this.startActivity(intent);
            }
        });
    }

    public void onClick(View paramView) {
        int i = paramView.getId();
        if (i != R.id.buttonAddMoney) {
            if (i != R.id.buttonGoBack)
                return;
            startActivity(new Intent((Context)this, ProfileActivity.class));
            return;
        }
        addMoney();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_add_money);
        this.databaseMoney = FirebaseDatabase.getInstance().getReference("vehicles");
        this.editTextAddMoney = (EditText)findViewById(R.id.editTextAddMoney);
        this.editTextLicenseNo = (EditText)findViewById(R.id.editTextLicenseNo);
        this.progressBar = (ProgressBar)findViewById(R.id.progressBar);
        findViewById(R.id.buttonAddMoney).setOnClickListener(this);
        findViewById(R.id.buttonGoBack).setOnClickListener(this);
    }
}
