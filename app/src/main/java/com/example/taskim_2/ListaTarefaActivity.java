package com.example.taskim_2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskim_2.Adapters.TarefaAdapter;
import com.example.taskim_2.Dados.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class ListaTarefaActivity extends AppCompatActivity {
    private RecyclerView rvListaTarefa;
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listagemTarefas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lista);

        rvListaTarefa = findViewById(R.id.rvListaTarefa);
        rvListaTarefa.setLayoutManager(new LinearLayoutManager(this));

        listagemTarefas = new ArrayList<>();
        Tarefa t = new Tarefa();
        t.setConteudo("Teste"); t.setStatus(false);
        t.setId(1);
        listagemTarefas.add(t);
        listagemTarefas.add(t);
        listagemTarefas.add(t);
        listagemTarefas.add(t);

        tarefaAdapter = new TarefaAdapter(this);
        rvListaTarefa.setAdapter(tarefaAdapter);
        tarefaAdapter.setListagemTarefas(listagemTarefas);
    }
}
