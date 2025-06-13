package com.example.novoapp;

public class Posto {
    private String nome;
    private String cidade;
    private boolean dormir;
    private boolean comer;
    private boolean estacionamento;
    private boolean banheiroFeminino;
    private boolean banheiroMasculino;
    private boolean wifi;

    public Posto() {}

    public Posto(String nome, String cidade, boolean dormir, boolean comer, boolean estacionamento,
                 boolean banheiroFeminino, boolean banheiroMasculino, boolean wifi) {
        this.nome = nome;
        this.cidade = cidade;
        this.dormir = dormir;
        this.comer = comer;
        this.estacionamento = estacionamento;
        this.banheiroFeminino = banheiroFeminino;
        this.banheiroMasculino = banheiroMasculino;
        this.wifi = wifi;
    }

    public String getNome() { return nome; }
    public String getCidade() { return cidade; }
    public boolean isDormir() { return dormir; }
    public boolean isComer() { return comer; }
    public boolean isEstacionamento() { return estacionamento; }
    public boolean isBanheiroFeminino() { return banheiroFeminino; }
    public boolean isBanheiroMasculino() { return banheiroMasculino; }
    public boolean isWifi() { return wifi; }

    public void setNome(String nome) { this.nome = nome; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public void setDormir(boolean dormir) { this.dormir = dormir; }
    public void setComer(boolean comer) { this.comer = comer; }
    public void setEstacionamento(boolean estacionamento) { this.estacionamento = estacionamento; }
    public void setBanheiroFeminino(boolean banheiroFeminino) { this.banheiroFeminino = banheiroFeminino; }
    public void setBanheiroMasculino(boolean banheiroMasculino) { this.banheiroMasculino = banheiroMasculino; }
    public void setWifi(boolean wifi) { this.wifi = wifi; }
}
