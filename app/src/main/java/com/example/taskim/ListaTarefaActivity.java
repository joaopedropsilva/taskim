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

// Classe responsável pelo comportamento da atividade
// que exibe uma lista de tarefas de uma das listas
// previamente selecionadas na atividade principal
public class ListaTarefaActivity extends AppCompatActivity implements DialogCloseListener {
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listagemTarefas;
    private Database db;
    private int idLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Declaração dos componentes do layout
        // a serem utilizados
        RecyclerView rvListaTarefa;
        TextView tvNomeLista;
        FloatingActionButton btnAddTarefa;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_tarefa);

        // Criação da instância do handler responsável
        // pelo banco de dados da aplicação
        db = new Database(this);

        // Abertura do banco de dados da aplicação
        db.openDb();

        // Busca pelas informações da lista de tarefas
        // advindas da atividade principal e selecionada
        // pelo usuário, que estão contidas no Intent
        // de incialização da atividade
        Bundle listaInformation = getIntent().getExtras();

        // Busca pelo id e nome da lista
        // do conjunto de informações
        idLista = listaInformation.getInt("id_lista");
        String nomeLista = listaInformation.getString("nome_lista");

        // Instanciação do adapter responsável
        // pela lógica das tarefas de uma lista
        tarefaAdapter = new TarefaAdapter(this, db, idLista);

        // Inicialização da RecyclerView de tarefas a partir do layout
        rvListaTarefa = findViewById(R.id.rvListaTarefa);
        rvListaTarefa.setLayoutManager(new LinearLayoutManager(this));
        rvListaTarefa.setAdapter(tarefaAdapter);

        // Inicialização do título da lista de tarefas em questão a partir do layout
        tvNomeLista = findViewById(R.id.tvNomeLista);

        // Inicialização do botão de adição de tarefas a partir do layout
        btnAddTarefa = findViewById(R.id.btnAddTarefa);

        // Instanciação do helper que lida com a lógica do scroll
        // para os lados das tarefas
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new TarefaRecyclerItemTouchHelper(tarefaAdapter));
        itemTouchHelper.attachToRecyclerView(rvListaTarefa);

        // Busca no banco de dados pelas tarefas
        // disponíveis para a lista específica
        listagemTarefas = db.searchAllTarefaByIdLista(idLista);

        // Inversão a ordem da lista para exibir a última tarefa
        // adicionada em primeiro lugar
        Collections.reverse(listagemTarefas);

        // Incialização do nome da lista a ser exibido
        tvNomeLista.setText(nomeLista);
        tarefaAdapter.setListagemTarefas(listagemTarefas);

        // Criação do listener de click no botão de adição de tarefas
        btnAddTarefa.setOnClickListener(v -> {
            // Criação do fragmento de dialog de
            // adição de uma nova tarefa
            FragmentAddTarefa frgAddTarefa = new FragmentAddTarefa();

            // Incialização de um bundle
            // contendo o id da lista de tarefas
            // a ser passado para o fragmento
            Bundle bundle = new Bundle();
            bundle.putInt("id_lista", idLista);

            // Passagem dos argumentos para o fragmento
            frgAddTarefa.setArguments(bundle);

            // Função para abertura do fragmento recém criado
            frgAddTarefa.show(getSupportFragmentManager(), FragmentAddTarefa.TAG);
        });
    }

    // Implementação da callback necessária para o uso do FragmentAddTarefa
    // (fragmento de Dialog para adição/edição de tarefas) declaradas
    // na interface DialogCloseListener
    @Override
    public void handleDialogClose(DialogInterface dialogAddTarefa) {
        // Busca no banco refeita para exibir possíveis
        // novas tarefas adicionadas
        listagemTarefas = db.searchAllTarefaByIdLista(idLista);

        // Inversão a ordem da lista para exibir a última tarefa
        // adicionada em primeiro lugar
        Collections.reverse(listagemTarefas);

        // Passagem dos novos dados ao adapter
        tarefaAdapter.setListagemTarefas(listagemTarefas);

        // Notificação de mudança ocorrida dos dados
        // utilizado pela RecyclerView vinculada ao
        // adapter para exibir os novos dados
        tarefaAdapter.notifyDataSetChanged();
    }
}
