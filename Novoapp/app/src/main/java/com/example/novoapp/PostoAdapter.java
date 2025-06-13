package com.example.novoapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostoAdapter extends RecyclerView.Adapter<PostoAdapter.PostoViewHolder> {

    private List<Posto> lista;

    public PostoAdapter(List<Posto> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public PostoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_posto, parent, false);
        return new PostoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostoViewHolder holder, int position) {
        Posto posto = lista.get(position);

        Log.d("PostoAdapter", "Mostrando posto: " + posto.getNome());

        holder.nome.setText(posto.getNome());

        holder.icDormir.setVisibility(posto.isDormir() ? View.VISIBLE : View.GONE);
        holder.icComer.setVisibility(posto.isComer() ? View.VISIBLE : View.GONE);
        holder.icEstacionar.setVisibility(posto.isEstacionamento() ? View.VISIBLE : View.GONE);
        holder.icBanheiroFeminino.setVisibility(posto.isBanheiroFeminino() ? View.VISIBLE : View.GONE);
        holder.icBanheiroMasculino.setVisibility(posto.isBanheiroMasculino() ? View.VISIBLE : View.GONE);
        holder.cidade.setText(posto.getCidade());

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class PostoViewHolder extends RecyclerView.ViewHolder {
        TextView nome, cidade;
        ImageView icDormir, icComer,  icEstacionar, icBanheiroFeminino, icBanheiroMasculino;

        public PostoViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.txtNome);
            icDormir = itemView.findViewById(R.id.icDormir);
            icComer = itemView.findViewById(R.id.icComer);
            icEstacionar = itemView.findViewById(R.id.icEstacionar);
            icBanheiroFeminino = itemView.findViewById(R.id.icBanheiroFeminino);
            icBanheiroMasculino = itemView.findViewById(R.id.icBanheiroMasculino);
            cidade = itemView.findViewById(R.id.txtCidade);

        }
    }
}
