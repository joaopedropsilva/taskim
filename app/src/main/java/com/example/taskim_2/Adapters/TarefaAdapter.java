package com.example.taskim_2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskim_2.Dados.Tarefa;
import com.example.taskim_2.ListaTarefaActivity;
import com.example.taskim_2.R;

import java.util.ArrayList;
import java.util.List;

public class TarefaAdapter extends RecyclerView.Adapter<TarefaAdapter.ViewHolder> {
    private List<Tarefa> listagemTarefas;
    private ListaTarefaActivity listaTarefaActivity;

    public TarefaAdapter(ListaTarefaActivity listaTarefaActivity) {
        this.listaTarefaActivity = listaTarefaActivity;
        this.listagemTarefas = new ArrayList<>();
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
        Tarefa tarefa = listagemTarefas.get(position);

        CheckBox chk = viewHolder.getChkTarefa();

        chk.setText(tarefa.getConteudo());
        chk.setChecked(tarefa.getStatus());
    }

    @Override
    public int getItemCount() { return listagemTarefas.size(); }

    public void setListagemTarefas(List<Tarefa> listagemTarefas) {
        this.listagemTarefas = listagemTarefas;
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
