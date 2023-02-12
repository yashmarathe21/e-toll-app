package com.example.e_toll_android_app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckMoney extends AppCompatActivity implements View.OnClickListener {
    static int i = 1;

    Button checkMoney;

    EditText editTextLicenseNo;

    private TextView mValueView;

    private void CheckMoney() {
        String str = this.editTextLicenseNo.getText().toString().trim();
        if (str.isEmpty()) {
            this.editTextLicenseNo.setError("License Number is required");
            this.editTextLicenseNo.requestFocus();
            return;
        }
        FirebaseDatabase.getInstance().getReference().child("vehicles").child(str).child("money").addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError param1DatabaseError) {}

            public void onDataChange(DataSnapshot param1DataSnapshot) {
                Long long_ = (Long)param1DataSnapshot.getValue(Long.class);
                CheckMoney.this.mValueView.setText(long_.toString());
                if (CheckMoney.i > 1)
                    Toast.makeText((Context)CheckMoney.this, "Your vehicle was spotted at the toll", 1).show();
                CheckMoney.i++;
            }
        });
    }

    public void onClick(View paramView) {
        if (paramView.getId() != R.id.buttonCheckMoney)
            return;
        CheckMoney();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_check_money);
        this.mValueView = (TextView)findViewById(R.id.textViewMoney);
        this.editTextLicenseNo = (EditText)findViewById(R.id.editTextLicenseNo);
        findViewById(R.id.buttonCheckMoney).setOnClickListener(this);
    }
}
