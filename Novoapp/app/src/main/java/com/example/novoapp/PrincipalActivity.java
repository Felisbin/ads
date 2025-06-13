package com.example.novoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrincipalActivity extends AppCompatActivity {

    private TextView nomeTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logado);

        nomeTextView = findViewById(R.id.nome);
        ImageView backButton = findViewById(R.id.back_button);
        Button btnVerPostos = findViewById(R.id.btnVerPostos);
        Button btnCadastrarPosto = findViewById(R.id.btnCadastrarPosto);

        // Botão para abrir PostosActivity
        btnVerPostos.setOnClickListener(v -> {
            Intent intent = new Intent(PrincipalActivity.this, PostosActivity.class);
            startActivity(intent);
            finish();
        });

        // Botão para abrir CadastrarPostoActivity
        btnCadastrarPosto.setOnClickListener(v -> {
            Intent intent = new Intent(PrincipalActivity.this, CadastrarPostoActivity.class);
            startActivity(intent);
            finish();
        });

        // Botão de voltar
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios");
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();

            mDatabase.child(uid).child("nome").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    String nome = snapshot.getValue(String.class);
                    if (nome != null && !nome.isEmpty()) {
                        nomeTextView.setText("Bem vindo, " + nome + "!");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    nomeTextView.setText("Erro ao carregar nome.");
                }
            });
        }
    }
}
