package com.example.e_toll_android_app;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;

public class AddVehicle extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference databaseVehicle;

    EditText editTextAddMoney;

    EditText editTextLicenseNo;

    EditText editTextVehicleName;

    private ProgressDialog loadingBar;

    ProgressBar progressBar;

    private void addVehicle() {
        String str1 = this.editTextLicenseNo.getText().toString().trim();
        String str2 = this.editTextVehicleName.getText().toString();
        String str3 = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (str1.isEmpty()) {
            this.editTextLicenseNo.setError("License Number is required");
            this.editTextLicenseNo.requestFocus();
            return;
        }
        if (str2.isEmpty()) {
            this.editTextVehicleName.setError("Vehicle Name is required");
            this.editTextVehicleName.requestFocus();
            return;
        }
        validateVehicle(str1, str2, str3);
    }

    private void validateVehicle(final String licenseNo, final String vehicleName, final String email) {
        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        databaseVehicle.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError param1DatabaseError) {}

            public void onDataChange(DataSnapshot param1DataSnapshot) {
                if (!param1DataSnapshot.child("vehicles").child(licenseNo).exists()) {
                    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
                    hashMap.put("licenseNo", licenseNo);
                    hashMap.put("vehicleName", vehicleName);
                    hashMap.put("email", email);
                    RootRef.child("vehicles").child(licenseNo).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(Task<Void> param2Task) {
                            if (param2Task.isSuccessful()) {
                                Toast.makeText((Context)AddVehicle.this, "Your vehicle has been added", 0).show();
                                Intent intent = new Intent((Context)AddVehicle.this, ProfileActivity.class);
                                AddVehicle.this.startActivity(intent);
                                return;
                            }
                            Toast.makeText((Context)AddVehicle.this, "Network Error: Please try again after some time...", 0).show();
                        }
                    });
                    return;
                }
                AddVehicle addVehicle = AddVehicle.this;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("This ");
                stringBuilder.append(licenseNo);
                stringBuilder.append(" already exists.");
                Toast.makeText((Context)addVehicle, stringBuilder.toString(), 0).show();
                Toast.makeText((Context)AddVehicle.this, "Please enter your correct license no", 0).show();
                Intent intent = new Intent((Context)AddVehicle.this, ProfileActivity.class);
                AddVehicle.this.startActivity(intent);
            }
        });
    }

    public void onClick(View paramView) {
        int i = paramView.getId();
        if (i != R.id.buttonAddInformation) {
            if (i != R.id.buttonGoBack)
                return;
            startActivity(new Intent((Context)this, ProfileActivity.class));
            return;
        }
        addVehicle();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_add_vehicle);
        this.databaseVehicle = FirebaseDatabase.getInstance().getReference("vehicles");
        this.editTextLicenseNo = (EditText)findViewById(R.id.editTextLicenseNo);
        this.editTextVehicleName = (EditText)findViewById(R.id.editTextVehicleName);
        this.progressBar = (ProgressBar)findViewById(R.id.progressBar);
        findViewById(R.id.buttonAddInformation).setOnClickListener(this);
        findViewById(R.id.buttonGoBack).setOnClickListener(this);
    }
}
