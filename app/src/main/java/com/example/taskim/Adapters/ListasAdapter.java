package com.example.taskim.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskim.Dados.Lista;
import com.example.taskim.Handlers.Database;
import com.example.taskim.ListaTarefaActivity;
import com.example.taskim.ListagemListasActivity;
import com.example.taskim.R;

import java.util.List;

public class ListasAdapter extends RecyclerView.Adapter<ListasAdapter.ViewHolder> {
    private List<Lista> listagemListas;
    private final ListagemListasActivity listagemListasActivity;
    private final Database db;

    public List<Lista> getListagemListas() {
        return listagemListas;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvLista;

        public TextView getTvLista() {
            return tvLista;
        }

        public ViewHolder(View v) {
            super(v);
            tvLista = v.findViewById(R.id.tvLista);
        }
    }

    public ListasAdapter(ListagemListasActivity listagemListasActivity, Database database) {
        this.listagemListasActivity = listagemListasActivity;
        this.db = database;
    }

    @NonNull
    @Override
    public ListasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista, parent, false);
        return new ListasAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListasAdapter.ViewHolder viewHolder, int position) {
        // Inicialização do banco de dados
        db.openDb();

        // Recuperação da lista de tarefas em questão
        Lista lista = listagemListas.get(position);

        TextView tv = viewHolder.getTvLista();
        tv.setText(lista.getNome());

        // Listener de clique na TextView de lista de tarefas
        // que chama a atividade de ListaTarefaActivity para a
        // exibição das tarefas da lista
        tv.setOnClickListener(v -> {
            Handler handler = new Handler();
            Runnable startListaTarefaActivity = () -> {
                Intent intent = new Intent(
                        listagemListasActivity,
                        ListaTarefaActivity.class);

                // Inserção de argumentos no intent
                // para que a atividade seja incializada
                // corretamente e as tarefas estejam vinculadas
                // a lista correta
                intent.putExtra("id_lista", lista.getId());
                intent.putExtra("nome_lista", lista.getNome());

                listagemListasActivity.startActivity(intent);
            };

            handler.post(startListaTarefaActivity);
        });
    }

    @Override
    public int getItemCount() { return listagemListas.size(); }

    public Context getContext() { return listagemListasActivity; }

    public void setListagemListas(List<Lista> listagemListas) {
        this.listagemListas = listagemListas;
    }

    public void addLista(String nome) {
        Lista lista = new Lista();
        lista.setNome(nome);

        db.insertLista(lista);
    }

    public void deleteListaByIndex(int index) {
        Lista lista = listagemListas.get(index);

        db.deleteLista(lista.getId());
        listagemListas.remove(index);

        notifyItemRemoved(index);
    }

    public void editLista(Lista lista) {
        db.updateNomeLista(lista.getId(), lista.getNome());
    }
}
