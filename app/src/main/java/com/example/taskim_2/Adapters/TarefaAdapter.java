package com.example.taskim_2.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskim_2.FragmentAddTarefa;
import com.example.taskim_2.Dados.Tarefa;
import com.example.taskim_2.Handlers.Database;
import com.example.taskim_2.ListaTarefaActivity;
import com.example.taskim_2.R;

import java.util.List;

public class TarefaAdapter extends RecyclerView.Adapter<TarefaAdapter.ViewHolder> {
    private List<Tarefa> listagemTarefas;
    private final ListaTarefaActivity listaTarefaActivity;
    private final Database db;

    public TarefaAdapter(ListaTarefaActivity listaTarefaActivity, Database database) {
        this.listaTarefaActivity = listaTarefaActivity;
        this.db = database;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_tarefa, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        db.openDb();

        Tarefa tarefa = listagemTarefas.get(position);

        CheckBox chk = viewHolder.getChkTarefa();
        chk.setText(tarefa.getConteudo());
        chk.setChecked(tarefa.getStatus());

        chk.setOnCheckedChangeListener((buttonView, isChecked) -> {
            db.udpateStatusTarefa(tarefa.getId(), isChecked);
        });
    }

    @Override
    public int getItemCount() { return listagemTarefas.size(); }

    public Context getContext() { return listaTarefaActivity; }

    public void setListagemTarefas(List<Tarefa> listagemTarefas) {
        this.listagemTarefas = listagemTarefas;
    }

    public void deleteTarefaByIndex(int index) {
        Tarefa tarefa = listagemTarefas.get(index);

        db.deleteTarefa(tarefa.getId());
        listagemTarefas.remove(index);

        notifyItemRemoved(index);
    }

    public void editTarefaByIndex(int index) {
        Tarefa tarefa = listagemTarefas.get(index);

        // Passagem de dados da tarefa vindos da RecyclerView
        // para o fragmento que permite a edição da tarefa, por meio de um bundle
        Bundle bundle = new Bundle();
        bundle.putInt("id", tarefa.getId());
        bundle.putString("conteudo", tarefa.getConteudo());

        FragmentAddTarefa frgFragmentAddTarefa = new FragmentAddTarefa();
        frgFragmentAddTarefa.setArguments(bundle);
        frgFragmentAddTarefa.show(listaTarefaActivity.getSupportFragmentManager(), FragmentAddTarefa.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
         private final CheckBox chkTarefa;

        public CheckBox getChkTarefa() {
            return chkTarefa;
        }

        public ViewHolder(View v) {
            super(v);
            chkTarefa = v.findViewById(R.id.chkTarefa);
        }
    }
}
