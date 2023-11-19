package com.example.taskim.Dados;

public class Lista {
    private int id;
    private String nome;
    private boolean status;
    private int totalTarefas;
    private int totalTarefasCompletas;

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getTotalTarefas() {
        return totalTarefas;
    }

    public void setTotalTarefas(int totalTarefas) {
        this.totalTarefas = totalTarefas;
    }

    public int getTotalTarefasCompletas() {
        return totalTarefasCompletas;
    }

    public void setTotalTarefasCompletas(int totalTarefasCompletas) {
        this.totalTarefasCompletas = totalTarefasCompletas;
    }
}
