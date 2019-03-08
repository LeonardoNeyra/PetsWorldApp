package com.corcuera.neyra.petsworldapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.corcuera.neyra.petsworldapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText userEmail, userPass;
    private Button btnLogin;
    private ProgressBar loginProgress;
    private ImageView loginPhoto;

    private FirebaseAuth mAuth;

    private Intent HomeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = (EditText) findViewById(R.id.login_email);
        userPass = (EditText) findViewById(R.id.login_pass);
        btnLogin = (Button) findViewById(R.id.login_btn);
        loginProgress = (ProgressBar) findViewById(R.id.login_progress);
        loginPhoto = (ImageView) findViewById(R.id.registerPhoto);
        mAuth = FirebaseAuth.getInstance();
        HomeActivity = new Intent(this, com.corcuera.neyra.petsworldapp.Activities.HomeActivity.class);

        loginProgress.setVisibility(View.INVISIBLE);

        // Imagen de perfil
        loginPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerActivity);
                finish();
            }
        });

        // Boton Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = userEmail.getText().toString();
                final String pass = userPass.getText().toString();

                if (email.isEmpty() || pass.isEmpty()){
                    showToast("Todos los campos deben estar llenos");
                    loginProgress.setVisibility(View.VISIBLE);
                    btnLogin.setVisibility(View.INVISIBLE);
                }
                else {
                    signIn(email.trim(), pass.trim());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // Usuario ya esta logeado así que lo redireccionamos al HomeActivity
            updateUI();
        }
    }

    private void signIn(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                    updateUI();
                }
                else {
                    showToast("Error: " + task.getException().getMessage());
                    loginProgress.setVisibility(View.VISIBLE);
                    btnLogin.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUI() {
        startActivity(HomeActivity);
        finish();
    }


    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
