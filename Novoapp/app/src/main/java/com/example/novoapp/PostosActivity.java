package com.example.novoapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class PostosActivity extends AppCompatActivity {

    private RecyclerView recyclerPostos;
    private PostoAdapter adapter;
    private List<Posto> listaPostos = new ArrayList<>();

    private CheckBox checkDormir, checkComer;
    private CheckBox checkEstacionar, checkBanheirof, checkBanheirom, checkWifi;

    private TextView tvSemResultados;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postos);

        recyclerPostos = findViewById(R.id.recyclerPostos);
        checkDormir = findViewById(R.id.checkDormir);
        checkComer = findViewById(R.id.checkComer);
        checkEstacionar = findViewById(R.id.checkEstacionar);
        checkBanheirof = findViewById(R.id.checkBanheirof);
        checkBanheirom = findViewById(R.id.checkBanheirom);
        checkWifi = findViewById(R.id.checkWifi);
        tvSemResultados = findViewById(R.id.tvSemResultados);

        adapter = new PostoAdapter(listaPostos);
        recyclerPostos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPostos.setAdapter(adapter);

        View.OnClickListener filtroListener = v -> buscarPostos();

        checkDormir.setOnClickListener(filtroListener);
        checkComer.setOnClickListener(filtroListener);
        checkEstacionar.setOnClickListener(filtroListener);
        checkBanheirof.setOnClickListener(filtroListener);
        checkBanheirom.setOnClickListener(filtroListener);
        checkWifi.setOnClickListener(filtroListener);

        buscarPostos();
    }

    private void buscarPostos() {
        FirebaseDatabase.getInstance().getReference("postos")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaPostos.clear();

                        for (DataSnapshot d : snapshot.getChildren()) {
                            Posto posto = d.getValue(Posto.class);
                            if (posto == null) continue;

                            // Filtros:
                            if ((checkDormir.isChecked() && !posto.isDormir()) ||
                                    (checkComer.isChecked() && !posto.isComer()) ||
                                    (checkEstacionar.isChecked() && !posto.isEstacionamento()) ||
                                    (checkBanheirof.isChecked() && !posto.isBanheiroFeminino()) ||
                                    (checkBanheirom.isChecked() && !posto.isBanheiroMasculino()) ||
                                    (checkWifi.isChecked() && !posto.isWifi())) {
                                continue;
                            }

                            listaPostos.add(posto);
                        }

                        adapter.notifyDataSetChanged();

                        tvSemResultados.setVisibility(listaPostos.isEmpty() ? View.VISIBLE : View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PostosActivity.this, "Erro ao carregar dados: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
