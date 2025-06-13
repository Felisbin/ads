package com.example.novoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Redefinir extends AppCompatActivity {

    private RadioGroup opcaoRedefinicao;
    private RadioButton opcaoEmail, opcaoTelefone;
    private TextInputEditText input;
    private Button enviarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redefinir); // Altere para o nome do seu layout XML
        ImageView back_button3 = findViewById(R.id.back_button3);
        back_button3.setOnClickListener(v -> {
            Intent intent = new Intent(Redefinir.this, MainActivity.class);
            startActivity(intent);
            finish(); // finaliza a tela atual para não empilhar
        });
        opcaoRedefinicao = findViewById(R.id.opcaoRedefinicao); // CORRIGIDO
        opcaoEmail = findViewById(R.id.opcaoEmail);
        opcaoTelefone = findViewById(R.id.opcaoTelefone);
        input = findViewById(R.id.input);
        enviarButton = findViewById(R.id.enviarButton);

        enviarButton.setOnClickListener(v -> {
            String valorInput = input.getText().toString().trim();

            if (opcaoEmail.isChecked()) {
                enviarEmailRedefinicao(valorInput);
            } else if (opcaoTelefone.isChecked()) {
                enviarSmsCodigo(valorInput);
            } else {
                Toast.makeText(this, "Selecione uma opção", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Redefinição via EMAIL
    private void enviarEmailRedefinicao(String email) {
        if (TextUtils.isEmpty(email)) {
            input.setError("Digite o email");
            return;
        }

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Código enviado para o email.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Erro ao enviar: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Redefinição via TELEFONE (Requer configuração no Firebase Console)
    private void enviarSmsCodigo(String telefone) {
        if (TextUtils.isEmpty(telefone)) {
            input.setError("Digite o telefone");
            return;
        }

        // Validação do formato do telefone
        if (!telefone.startsWith("+55")) {
            input.setError("O número deve começar com o código do país (ex: +55 para Brasil)");
            return;
        }

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(telefone)       // Número do telefone
                        .setTimeout(60L, TimeUnit.SECONDS)   // Timeout de 60 segundos
                        .setActivity(this)               // A Activity atual
                        .setCallbacks(callbacks)         // Callbacks para tratamento
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    // Callbacks para verificação via telefone
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    // Autenticado automaticamente (em alguns casos)
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(Redefinir.this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    // Código enviado com sucesso — ir para próxima tela
                    Intent intent = new Intent(Redefinir.this, NovaSenha.class);
                    intent.putExtra("verificationId", verificationId);
                    startActivity(intent);
                }
            };
}
