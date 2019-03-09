package com.corcuera.neyra.petsworldapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.corcuera.neyra.petsworldapp.Models.Pet;
import com.corcuera.neyra.petsworldapp.Models.Post;
import com.corcuera.neyra.petsworldapp.Models.User;
import com.corcuera.neyra.petsworldapp.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddpetActivity extends AppCompatActivity {

    private EditText petName, petColor, phoneNumber, petComents;
    private RadioButton rbtnMacho, rbtnHembra;
    private Spinner spnTipo, spnPeso;
    private Button btnAceptar, btnCancelar;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpet);

        showToast("Estamos aqui");

        // Instancia de firebase user
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Obtener elementos del View
        petName = (EditText) findViewById(R.id.addpet_MName);
        petColor = (EditText) findViewById(R.id.addpet_MColor);
        phoneNumber = (EditText) findViewById(R.id.addpet_MCelular);
        petComents = (EditText) findViewById(R.id.addpet_MComentario);
        rbtnMacho = (RadioButton) findViewById(R.id.addpet_PMacho);
        rbtnHembra = (RadioButton) findViewById(R.id.addpet_PHembra);
        spnTipo = (Spinner) findViewById(R.id.addpet_MTipo);
        spnPeso = (Spinner) findViewById(R.id.addpet_MPeso);
        btnAceptar = (Button) findViewById(R.id.addpet_BtnAgregar);
        btnCancelar = (Button) findViewById(R.id.addpet_BtnCancelar);

        //Iniciar FirebaseDatabase
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // Opciones de los spinners
        String [] opc_Tipo = {"Gato", "Perro", "Loro", "Pez", "Rata", "Conejo"};
        String [] opc_Peso = {"<100gr", "100gr-500gr", ">500gr"};

        // Adapters para los spinners
        ArrayAdapter<String> adapter_Tape = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opc_Tipo);
        spnTipo.setAdapter(adapter_Tape);
        ArrayAdapter<String> adapterPeso = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opc_Peso);
        spnPeso.setAdapter(adapterPeso);

        // Boton Agregar post
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nombre = petName.getText().toString();
                final String color = petColor.getText().toString();
                final String numero = phoneNumber.getText().toString();
                final String comentario = petComents.getText().toString();
                final String tipo = spnTipo.getSelectedItem().toString();
                final String peso = spnPeso.getSelectedItem().toString();

                if (nombre.isEmpty() || color.isEmpty() || numero.isEmpty() || comentario.isEmpty() || (rbtnHembra.isChecked() == false && rbtnMacho.isChecked() ==false) ) {
                    showToast("Llene todos los campos");
                    validation();
                }
                else {
                    // Creamos un uid general
                    String generalUID = UUID.randomUUID().toString();

                    // Creamos el objeto Mascota
                    Pet pet = new Pet();
                    pet.setNombre(nombre);
                    if (rbtnHembra.isChecked()){
                        pet.setGenero(rbtnHembra.getText().toString());
                    }
                    else if (rbtnMacho.isChecked()) {
                        pet.setGenero(rbtnMacho.getText().toString());
                    }
                    pet.setTipo(tipo);
                    pet.setPeso(peso);
                    pet.setColor(color);
                    pet.setComentario(comentario);
                    // Agregamos un nodo "Pet"
                    databaseReference.child("Pet").child(generalUID).setValue(pet);

                    // Creamos el objeto User
                    User user = new User();
                    user.setUid(generalUID);
                    user.setUsername(currentUser.getDisplayName());
                    user.setCorreo(currentUser.getEmail());
                    // Agregamos un nodo "User"
                    databaseReference.child("User").child(generalUID).setValue(user);

                    // Creamos el objeto Post
                    Post post = new Post();
                    post.setUid(generalUID);
                    post.setMascota(pet);
                    post.setUsuario(user);
                    // Agregamos un nodo "Post"
                    databaseReference.child("Post").child(generalUID).setValue(post);

                    showToast("Post de tu mascota publicado :D");

                    goToAddFragment();
                }
            }
        });

        // Boton Cancelar
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
                showToast("Cancelado");
                goToAddFragment();
            }
        });

    }

    private void goToAddFragment() {
        Intent intent = new Intent(AddpetActivity.this, HomeNav.class);
        startActivity(intent);
        finish();
    }

    private void clearFields() {
        petName.setText("");
        petColor.setText("");
        phoneNumber.setText("");
        petComents.setText("");
        rbtnHembra.setChecked(false);
        rbtnMacho.setChecked(false);
        spnTipo.setSelected(true);
        spnPeso.setSelected(true);
    }

    private void validation() {
        final String nombre = petName.getText().toString();
        final String color = petColor.getText().toString();
        final String numero = phoneNumber.getText().toString();
        final String comentario = petComents.getText().toString();
        if (nombre.isEmpty()) {
            petName.setError("Required");
        }
        if (color.isEmpty()) {
            petName.setError("Required");
        }
        if (numero.isEmpty()) {
            petName.setError("Required");
        }
        if (comentario.isEmpty()) {
            petName.setError("Required");
        }
    }

    private void showToast(String message) {
        Toast.makeText(AddpetActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
