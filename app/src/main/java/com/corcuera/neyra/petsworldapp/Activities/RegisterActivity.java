package com.corcuera.neyra.petsworldapp.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.Manifest;
import android.widget.Toast;

//import com.corcuera.neyra.petsworldapp.Manifest;
import com.corcuera.neyra.petsworldapp.R;

public class RegisterActivity extends AppCompatActivity {

    static int ReqCode = 1;
    Uri pickedImgUri;
    ImageView ImgUserPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImgUserPhoto = (ImageView) findViewById(R.id.registerPhoto);

        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkForPermission();
                }
                else {
                    openGallery();
                }
            }
        });
    }

    private void checkForPermission(){
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(RegisterActivity.this, "Por favor acepte los permisos", Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, ReqCode);
            }
        }
        else {
            openGallery();
        }
    }

    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, ReqCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == ReqCode && data != null) {
            pickedImgUri = data.getData();
            ImgUserPhoto.setImageURI(pickedImgUri);
        }
    }
}
