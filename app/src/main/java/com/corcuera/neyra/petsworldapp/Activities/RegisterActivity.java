package com.corcuera.neyra.petsworldapp.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.Manifest;
import android.widget.ProgressBar;
import android.widget.Toast;

//import com.corcuera.neyra.petsworldapp.Manifest;
import com.corcuera.neyra.petsworldapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    static int ReqCode = 1;
    Uri pickedImgUri;

    private ImageView ImgUserPhoto;
    private EditText userName, userMail, userPass;
    private Button regBtn;
    private ProgressBar loading;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Iniciar elementos
        ImgUserPhoto = (ImageView) findViewById(R.id.registerPhoto);
        userName = (EditText) findViewById(R.id.registerName);
        userMail = (EditText) findViewById(R.id.registerEmail);
        userPass = (EditText) findViewById(R.id.registerPass);
        regBtn = (Button) findViewById(R.id.registerBtn);
        loading = (ProgressBar) findViewById(R.id.progressBar);
        loading.setVisibility(View.INVISIBLE);

        // Instanciar Firebase
        mAuth = FirebaseAuth.getInstance();


        // Boton Registrar
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regBtn.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.VISIBLE);
                final String name = userName.getText().toString();
                final String email = userMail.getText().toString();
                final String pass = userPass.getText().toString();

                if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                    showToast("Todos los campos deben estar llenos");
                    regBtn.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                }
                else {
                    createUserAcc(email.trim(), pass.trim(), name.trim());
                }
            }
        });


        // Imagen de perfil
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
                showToast("Por favor acepte los permisos");
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

    private void createUserAcc(String email, String pass, final String name){
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Cuenta de usuario creado exitosamente
                            showToast("Cuenta creada");
                            // Luego de crear la cuenta necesitamos actualizar su foto y nombre de perfil
                            updateUserInfoAcc(name, pickedImgUri, mAuth.getCurrentUser());
                        }
                        else {
                            // Cuenta de usuario fallida
                            showToast("Creación de cuenta fallida: " + task.getException().getMessage());
                            regBtn.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void updateUserInfoAcc(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        // Necesitamos actualizar la foto al almacenamiento de Firebase y obtener su url
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Fotos subida satisfactoriamente
                // Ahora obtenemos la url de la imagen
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // uri contiene la url de la imgane de usuario
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileChangeRequest)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Informacion de usuario actualizada
                                            showToast("Registro Completo");
                                            updateUri();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    private void updateUri() {
        Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(homeActivity);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
