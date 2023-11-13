package com.example.taskim_2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskim_2.Adapters.TarefaAdapter;
import com.example.taskim_2.Dados.Tarefa;
import com.example.taskim_2.Handlers.Database;
import com.example.taskim_2.Handlers.DialogCloseListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class ListaTarefaActivity extends AppCompatActivity implements DialogCloseListener {
    private RecyclerView rvListaTarefa;
    private FloatingActionButton btnAddTarefa;
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listagemTarefas;

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lista);

        db = new Database(this);
        db.openDb();

        rvListaTarefa = findViewById(R.id.rvListaTarefa);
        rvListaTarefa.setLayoutManager(new LinearLayoutManager(this));

        btnAddTarefa = findViewById(R.id.btnAddTarefa);

        tarefaAdapter = new TarefaAdapter(this, db);
        rvListaTarefa.setAdapter(tarefaAdapter);
        tarefaAdapter.setListagemTarefas(listagemTarefas);

        listagemTarefas = db.searchAllTarefa();
        // Inverte a ordem da lista para exibir a última tarefa
        // adicionada em primeiro lugar
        Collections.reverse(listagemTarefas);

        tarefaAdapter.setListagemTarefas(listagemTarefas);

        btnAddTarefa.setOnClickListener(v -> {
            AddTarefa frgAddTarefa = new AddTarefa();
            frgAddTarefa.show(getSupportFragmentManager(), AddTarefa.TAG);
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialogAddTarefa) {
        listagemTarefas = db.searchAllTarefa();
        // Inverte a ordem da lista para exibir a última tarefa
        // adicionada em primeiro lugar
        Collections.reverse(listagemTarefas);

        tarefaAdapter.setListagemTarefas(listagemTarefas);
        tarefaAdapter.notifyItemInserted(listagemTarefas.size() - 1);
    }
}
