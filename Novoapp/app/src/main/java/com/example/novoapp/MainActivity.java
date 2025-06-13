package com.example.novoapp;

import android.content.Intent; // para você poder usar a classe Intent no Android, que serve para navegar entre telas (Activities).
import android.os.Bundle; // Gerenciar dados durante a criação de uma Activity.
import android.text.TextUtils; //  ajuda a trabalhar com texto no Android de forma mais segura, principalmente para verificar se campos de entrada estão vazios.
import android.view.View; //Representar qualquer elemento de interface (como botões, textos, imagens).//guarda valores temporários para que a tela não perca estado após eventos como rotação
import android.widget.Button;// Permite que você crie e use botões no seu app e defina ações quando o usuário clicar neles.
import android.widget.EditText; //serve para importar o componente de entrada de texto do Android, chamado EditText.
import android.widget.TextView;// Mostrar texto na tela.
import android.widget.Toast;// É uma forma simples de dar um aviso ao usuário, sem abrir uma nova janela ou alerta.


import androidx.appcompat.app.AppCompatActivity; //Base para criar Activities modernas, com suporte a temas.

import com.google.firebase.auth.FirebaseAuth;  // Lidar com login/autenticação de usuários (email, Google, etc.).

public class MainActivity extends AppCompatActivity {

    EditText editTextUsername; //variaveis
    EditText editTextTextPassword; //variaveis
    Button buttonLogin;//variaveis
    Button buttonRegister;//variaveis
    TextView linkEsqueceuSenha;//variaveis

    private FirebaseAuth mAuth;//variaveis

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //  chama a versão da superclasse (obrigatório).
        setContentView(R.layout.activity_main);  // Carrega o layout da tela de logado
        // Firebase Auth
        mAuth = FirebaseAuth.getInstance(); // pega a instancia e joga dentro da variavel mauth pra usar quando quiser

        // Conectar os elementos da interface
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        linkEsqueceuSenha = findViewById(R.id.TextViewForgotPassword);

        // Botão de login
        buttonLogin.setOnClickListener(v -> {
            String email = editTextUsername.getText().toString().trim();
            String senha = editTextTextPassword.getText().toString().trim();

            //validar se o usuário colocou e-mail ou senha
            if (TextUtils.isEmpty(email)) {
                editTextUsername.setError("Digite o e-mail");
                editTextUsername.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(senha)) {
                editTextTextPassword.setError("Digite a senha");
                editTextTextPassword.requestFocus();
                return;
            }

            // Autenticar com Firebase
            mAuth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

                            // Ir para tela principal
                            Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
                            startActivity(intent);
                            finish(); // finaliza a tela de login
                        } else {
                            Toast.makeText(MainActivity.this, "Erro ao fazer login: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Botão cadastrar que vai pra tela de cadastro
        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
            startActivity(intent);
        });

        // Link "Esqueceu a senha?"
        linkEsqueceuSenha.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Redefinir.class);
            startActivity(intent);
        });
    }
}
