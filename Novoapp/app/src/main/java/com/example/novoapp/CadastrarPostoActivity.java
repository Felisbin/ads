package com.example.novoapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastrarPostoActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editCidade;
    private CheckBox checkDormir, checkComer, checkBanheiroFeminino, checkBanheiroMasculino, checkWifi, checkEstacionamento;
    private Button btnSalvar;

    private DatabaseReference databasePostos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_posto);

        // Referencia os componentes do layout
        editNome = findViewById(R.id.txtNome);
        editCidade = findViewById(R.id.txtCidade);
        checkDormir = findViewById(R.id.checkDormir);
        checkComer = findViewById(R.id.checkComer);
        checkBanheiroFeminino = findViewById(R.id.checkBanheirofeminino);
        checkBanheiroMasculino = findViewById(R.id.checkBanheiromasculino);
        checkWifi = findViewById(R.id.checkWifi);
        checkEstacionamento = findViewById(R.id.checkEstacionamento);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Inicializa a referência do Firebase
        databasePostos = FirebaseDatabase.getInstance().getReference("postos");

        // Define a ação do botão salvar
        btnSalvar.setOnClickListener(v -> salvarPosto());
    }

    private void salvarPosto() {
        String nome = editNome.getText().toString().trim();
        String cidade = editCidade.getText().toString().trim();

        if (TextUtils.isEmpty(nome)) {
            editNome.setError("Informe o nome do posto");
            editNome.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(cidade)) {
            editCidade.setError("Informe a cidade");
            editCidade.requestFocus();
            return;
        }

        // Gera um ID único
        String id = databasePostos.push().getKey();
        if (id == null) {
            Toast.makeText(this, "Erro ao gerar ID do posto", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cria o objeto Posto com cidade
        Posto novoPosto = new Posto(id, nome, cidade,
                checkDormir.isChecked(),
                checkComer.isChecked(),
                checkBanheiroFeminino.isChecked(),
                checkBanheiroMasculino.isChecked(),
                checkWifi.isChecked(),
                checkEstacionamento.isChecked());

        // Salva no Firebase
        databasePostos.child(id).setValue(novoPosto)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CadastrarPostoActivity.this, "Posto cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                        limparCampos();
                    } else {
                        Toast.makeText(CadastrarPostoActivity.this, "Erro ao cadastrar posto", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void limparCampos() {
        editNome.setText("");
        editCidade.setText("");
        checkDormir.setChecked(false);
        checkComer.setChecked(false);
        checkBanheiroFeminino.setChecked(false);
        checkBanheiroMasculino.setChecked(false);
        checkWifi.setChecked(false);
        checkEstacionamento.setChecked(false);
    }

    // Modelo de dados atualizado com cidade
    public static class Posto {
        public String id;
        public String nome;
        public String cidade;
        public boolean dormir;
        public boolean comer;
        public boolean banheiroFeminino;
        public boolean banheiroMasculino;
        public boolean wifi;
        public boolean estacionamento;

        public Posto() {
            // Requerido para o Firebase
        }

        public Posto(String id, String nome, String cidade, boolean dormir, boolean comer,
                     boolean banheiroFeminino, boolean banheiroMasculino,
                     boolean wifi, boolean estacionamento) {
            this.id = id;
            this.nome = nome;
            this.cidade = cidade;
            this.dormir = dormir;
            this.comer = comer;
            this.banheiroFeminino = banheiroFeminino;
            this.banheiroMasculino = banheiroMasculino;
            this.wifi = wifi;
            this.estacionamento = estacionamento;
        }
    }
}
