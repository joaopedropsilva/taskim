package com.example.taskim;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskim.Adapters.TarefaAdapter;
import com.example.taskim.Dados.Tarefa;
import com.example.taskim.Fragments.FragmentAddTarefa;
import com.example.taskim.Handlers.Database;
import com.example.taskim.Handlers.DialogCloseListener;
import com.example.taskim.Helpers.TarefaRecyclerItemTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class ListaTarefaActivity extends AppCompatActivity implements DialogCloseListener {
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listagemTarefas;
    private Database db;
    private int idLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView rvListaTarefa;
        TextView tvNomeLista;
        FloatingActionButton btnAddTarefa;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_tarefa);

        db = new Database(this);
        db.openDb();

        Bundle listaInformation = getIntent().getExtras();
        idLista = listaInformation.getInt("id_lista");
        String nomeLista = listaInformation.getString("nome_lista");

        tarefaAdapter = new TarefaAdapter(this, db, idLista);

        rvListaTarefa = findViewById(R.id.rvListaTarefa);
        rvListaTarefa.setLayoutManager(new LinearLayoutManager(this));
        rvListaTarefa.setAdapter(tarefaAdapter);

        tvNomeLista = findViewById(R.id.tvNomeLista);
        btnAddTarefa = findViewById(R.id.btnAddTarefa);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TarefaRecyclerItemTouchHelper(tarefaAdapter));
        itemTouchHelper.attachToRecyclerView(rvListaTarefa);

        listagemTarefas = db.searchAllTarefaByIdLista(idLista);
        // Inverte a ordem da lista para exibir a última tarefa
        // adicionada em primeiro lugar
        Collections.reverse(listagemTarefas);

        tvNomeLista.setText(nomeLista);
        tarefaAdapter.setListagemTarefas(listagemTarefas);

        btnAddTarefa.setOnClickListener(v -> {
            FragmentAddTarefa frgAddTarefa = new FragmentAddTarefa();

            Bundle bundle = new Bundle();
            bundle.putInt("id_lista", idLista);

            frgAddTarefa.setArguments(bundle);
            frgAddTarefa.show(getSupportFragmentManager(), FragmentAddTarefa.TAG);
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialogAddTarefa) {
        listagemTarefas = db.searchAllTarefaByIdLista(idLista);
        // Inverte a ordem da lista para exibir a última tarefa
        // adicionada em primeiro lugar
        Collections.reverse(listagemTarefas);

        tarefaAdapter.setListagemTarefas(listagemTarefas);
        tarefaAdapter.notifyDataSetChanged();
    }
}
