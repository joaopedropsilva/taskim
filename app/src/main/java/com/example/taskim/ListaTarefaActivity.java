package com.example.taskim;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskim.Adapters.TarefaAdapter;
import com.example.taskim.Dados.Tarefa;
import com.example.taskim.Handlers.Database;
import com.example.taskim.Handlers.DialogCloseListener;
import com.example.taskim.Handlers.TarefaRecyclerItemTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class ListaTarefaActivity extends AppCompatActivity implements DialogCloseListener {
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listagemTarefas;

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView rvListaTarefa;
        FloatingActionButton btnAddTarefa;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lista);

        db = new Database(this);
        db.openDb();

        tarefaAdapter = new TarefaAdapter(this, db);

        rvListaTarefa = findViewById(R.id.rvListaTarefa);
        rvListaTarefa.setLayoutManager(new LinearLayoutManager(this));
        rvListaTarefa.setAdapter(tarefaAdapter);

        btnAddTarefa = findViewById(R.id.btnAddTarefa);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TarefaRecyclerItemTouchHelper(tarefaAdapter));
        itemTouchHelper.attachToRecyclerView(rvListaTarefa);


        listagemTarefas = db.searchAllTarefa();
        // Inverte a ordem da lista para exibir a última tarefa
        // adicionada em primeiro lugar
        Collections.reverse(listagemTarefas);

        tarefaAdapter.setListagemTarefas(listagemTarefas);

        btnAddTarefa.setOnClickListener(v -> {
            FragmentAddTarefa frgAddTarefa = new FragmentAddTarefa();
            frgAddTarefa.show(getSupportFragmentManager(), FragmentAddTarefa.TAG);
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialogAddTarefa) {
        listagemTarefas = db.searchAllTarefa();
        // Inverte a ordem da lista para exibir a última tarefa
        // adicionada em primeiro lugar
        Collections.reverse(listagemTarefas);

        tarefaAdapter.setListagemTarefas(listagemTarefas);
        tarefaAdapter.notifyDataSetChanged();
    }
}
