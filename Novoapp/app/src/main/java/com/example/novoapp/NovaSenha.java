package com.example.novoapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class NovaSenha extends AppCompatActivity {

    private EditText etCode, etNewPassword, etConfirmPassword;
    private Button btnConfirm;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_senha);

        etCode = findViewById(R.id.etCode);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnConfirm = findViewById(R.id.btnConfirm);

        // Recebe o verificationId da tela anterior
        verificationId = getIntent().getStringExtra("verificationId");

        btnConfirm.setOnClickListener(v -> {
            String code = etCode.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                return;
            }

            // Autentica o código recebido por SMS
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(updateTask -> {
                                            if (updateTask.isSuccessful()) {
                                                Toast.makeText(this, "Senha redefinida com sucesso!", Toast.LENGTH_SHORT).show();
                                                finish(); // Ou redirecionar para LoginActivity
                                            } else {
                                                Toast.makeText(this, "Erro ao redefinir senha: " + updateTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(this, "Código inválido ou expirado", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
