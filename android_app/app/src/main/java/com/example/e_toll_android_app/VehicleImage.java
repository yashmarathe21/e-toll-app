package com.example.e_toll_android_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;

public class VehicleImage extends AppCompatActivity implements View.OnClickListener {
    Button checkVehicle;

    EditText editTextLicenseNo;

    ImageView image;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    private void findImage() {
        String str = this.editTextLicenseNo.getText().toString().trim();
        if (str.isEmpty()) {
            this.editTextLicenseNo.setError("License Number is required");
            this.editTextLicenseNo.requestFocus();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(".jpg");
        str = stringBuilder.toString();
        try {
            final File file = File.createTempFile("image", "jpg");
            this.storage.getReferenceFromUrl("gs://project03-c99a3.appspot.com/").child(str).getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                public void onSuccess(FileDownloadTask.TaskSnapshot param1TaskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    VehicleImage.this.image.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                public void onFailure(Exception param1Exception) {
                    Toast.makeText((Context)VehicleImage.this, "Image failed to load", 0).show();
                }
            });
            return;
        } catch (IOException iOException) {
            iOException.printStackTrace();
            return;
        }
    }

    public void onClick(View paramView) {
        if (paramView.getId() != R.id.buttonCheckVehicle)
            return;
        findImage();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_vehicle_image);
        this.editTextLicenseNo = (EditText)findViewById(R.id.editTextLicenseNo);
        this.image = (ImageView)findViewById(R.id.image);
        findViewById(R.id.buttonCheckVehicle).setOnClickListener(this);
    }
}
