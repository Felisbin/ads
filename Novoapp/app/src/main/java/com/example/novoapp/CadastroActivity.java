package com.example.novoapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class CadastroActivity extends AppCompatActivity {

    private EditText editTextNome, editTextEmail, editTextSenha, editTextConfirmarSenha, editTextTelefone, editTextDataEmissao;
    private CheckBox checkBoxTermos;

    private CheckBox checkMasculino;

    private CheckBox checkFeminino;

    private CheckBox checkOutros;

    private Button buttonCadastrar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        ImageView back_button2 = findViewById(R.id.back_button2);
        back_button2.setOnClickListener(v -> {
            Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("usuarios");

        // Componentes da tela
        editTextNome = findViewById(R.id.editTextNome);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        editTextConfirmarSenha = findViewById(R.id.editTextConfirmarSenha);
        editTextTelefone = findViewById(R.id.editTextTelefone);
        editTextDataEmissao = findViewById(R.id.editTextDataEmissao);
        checkMasculino = findViewById(R.id.checkMasculino);
        checkFeminino = findViewById(R.id.checkFeminino);
        checkOutros = findViewById(R.id.checkOutros);
        checkBoxTermos = findViewById(R.id.checkBoxTermos);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);

        // Configurar DatePicker para Data de Emissão CNH
        editTextDataEmissao.setFocusable(false);
        editTextDataEmissao.setClickable(true);
        editTextDataEmissao.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (DatePicker view, int year1, int month1, int dayOfMonth) -> {
                String dataFormatada = String.format("%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                editTextDataEmissao.setText(dataFormatada);
            }, year, month, day);

            datePickerDialog.show();
        });

        buttonCadastrar.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        String nome = editTextNome.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String senha = editTextSenha.getText().toString().trim();
        String confirmarSenha = editTextConfirmarSenha.getText().toString().trim();
        String telefone = editTextTelefone.getText().toString().trim();
        String dataEmissao = editTextDataEmissao.getText().toString().trim();

        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)
                || TextUtils.isEmpty(confirmarSenha) || TextUtils.isEmpty(telefone)) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkBoxTermos.isChecked()) {
            Toast.makeText(this, "Você precisa aceitar os termos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar se o e-mail já está em uso
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().getSignInMethods().isEmpty()) {
                            // O e-mail não está em uso, podemos criar a conta
                            criarConta(email, senha, nome, telefone, dataEmissao);
                        } else {
                            Toast.makeText(this, "Este e-mail já está cadastrado. Tente fazer login ou usar outro e-mail.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "Erro ao verificar e-mail: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void criarConta(String email, String senha, String nome, String telefone, String dataEmissao) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();

                            HashMap<String, String> userData = new HashMap<>();
                            userData.put("nome", nome);
                            userData.put("email", email);
                            userData.put("telefone", telefone);
                            userData.put("dataEmissaoCNH", dataEmissao);

                            mDatabase.child(userId).setValue(userData)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                                            limparCampos();
                                        } else {
                                            Toast.makeText(this, "Erro ao salvar dados: " + dbTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(this, "Erro ao cadastrar: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void limparCampos() {
        editTextNome.setText("");
        editTextEmail.setText("");
        editTextSenha.setText("");
        editTextConfirmarSenha.setText("");
        editTextTelefone.setText("");
        editTextDataEmissao.setText("");
        checkBoxTermos.setChecked(false);
        checkMasculino.setChecked(false);
        checkFeminino.setChecked(false);
        checkOutros.setChecked(false);

    }
}
