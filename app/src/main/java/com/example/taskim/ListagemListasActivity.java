package com.example.taskim;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskim.Adapters.ListasAdapter;
import com.example.taskim.Dados.Lista;
import com.example.taskim.Handlers.Database;
import com.example.taskim.Helpers.ListaRecyclerItemTouchHelper;
import com.example.taskim.Fragments.DialogEditText;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Collections;
import java.util.List;

// Classe responsável pelo comportamento da atividade
// principal que exibe as listas de tarefa
public class ListagemListasActivity extends AppCompatActivity
        implements DialogEditText.DialogEditTextListener {
    private ListasAdapter listasAdapter;
    private List<Lista> listagemListas;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Declaração dos componentes do layout
        // a serem utilizados
        RecyclerView rvListagemListas;
        ExtendedFloatingActionButton btnAddLista;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.listagem_listas);

        // Criação da instância do handler responsável
        // pelo banco de dados da aplicação
        db = new Database(this);

        // Abertura do banco de dados da aplicação
        db.openDb();

        // Instanciação do adapter responsável
        // pela lógica das listas de tarefas
        listasAdapter = new ListasAdapter(this, db);

        // Inicialização da RecyclerView de listas de tarefa a partir do layout
        rvListagemListas = findViewById(R.id.rvListagemListas);
        rvListagemListas.setLayoutManager(new LinearLayoutManager(this));
        rvListagemListas.setAdapter(listasAdapter);

        // Inicialização do botão de adição de listas de tarefa a partir do layout
        btnAddLista = findViewById(R.id.btnAddLista);

        // Instanciação do helper que lida com a lógica do scroll
        // para os lados das listas de tarefa
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ListaRecyclerItemTouchHelper(listasAdapter));
        itemTouchHelper.attachToRecyclerView(rvListagemListas);

        // Busca no banco de dados pelas listas disponíveis
        listagemListas = db.searchAllListas();

        // Inversão da ordem das listas encontradas para exibir
        // a última lista adicionada em primeiro lugar
        Collections.reverse(listagemListas);

        listasAdapter.setListagemListas(listagemListas);

        // Criação do listener de click no botão de adição de listas
        btnAddLista.setOnClickListener(v -> {
            // Criação do fragmento de dialog de
            // adição de uma nova lista
            DialogEditText dialog = new DialogEditText(
                    "Adicionar nova lista",
                    "Adicionar",
                    "Cancelar");

            // Ao executar uma adição de uma lista
            // um bundle é incializado com argumentos
            // para que o se possa saber qual é a operação que o
            // usuário deseja fazer e que as funções corretas
            // do adapter sejam chamadas
            Bundle args = new Bundle();
            args.putString("operation", "add");

            // Passagem dos argumentos para o fragmento
            dialog.setArguments(args);

            // Função para abertura do fragmento recém criado
            dialog.show(getSupportFragmentManager(), DialogEditText.TAG);
        });
    }

    // Implementação realizada para prevenir a reabertura
    // dos fragmentos de adição/edição de listas de tarefas
    // quando a atividade principal sai de contexto e volta,
    // posteriormente, a ser executada
    @Override
    public void onResumeFragments() {
        super.onResumeFragments();

        // Instanciação do manager de fragmentos
        // e coleta do fragmento indesejado
        FragmentManager frgManager = getSupportFragmentManager();
        Fragment fragment = frgManager.findFragmentByTag(DialogEditText.TAG);

        // Verificação para inicialização do aplicativo
        // quando não há fragmentos abertos
        if (fragment != null) {
            // Execução da transação que remove
            // o fragmento indesejado da tela
            frgManager.beginTransaction().remove(fragment).commit();
        }
    }

    // Implementações dos callbacks necessários para o uso do DialogEditText
    // (fragmento de Dialog para adição/edição de listas de tarefas) declaradas
    // na interface DialogEditText.DialogEditTextListener

    // Callback executada ao confirmar a adição/edição de lista
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // Busca nos argumentos fragmento de Dialog qual
        // operação o usuário deseja realizar
        String operation = dialog.getArguments().getString("operation");

        // Busca pelo id da lista envolvida na edição de lista
        // caso a operação seja uma edição
        int idLista = dialog.getArguments().getInt("id");

        // Busca pelo EditText do Dialog com o conteúdo (nome)
        // da lista a ser adicionada/editada
        EditText edtDialogLista = dialog.getDialog().findViewById(R.id.edtInput);

        if (edtDialogLista != null) {
            // Busca pelo texto digitado no EditText no momento
            String nomeLista = edtDialogLista.getText().toString();

            // Execução de rotinas de adição/edição
            switch (operation) {
                case "add":
                    listasAdapter.addLista(nomeLista);
                    break;
                case "edit":
                    Lista lista = new Lista();
                    lista.setId(idLista);
                    lista.setNome(nomeLista);

                    listasAdapter.editLista(lista);
                    break;
                default:
            }
        }

        // Esconde o Dialog de adição/edição
        dialog.getDialog().dismiss();
    }

    // Callback executada ao cancelar a adição/edição de lista
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {}

    // Callback executada ao fechar a adição/edição de lista
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        // Busca no banco refeita para exibir possíveis
        // novas listas adicionadas
        listagemListas = db.searchAllListas();

        // Inversão a ordem da lista para exibir
        // a última lista adicionada em primeiro lugar
        Collections.reverse(listagemListas);

        // Passagem dos novos dados ao adapter
        listasAdapter.setListagemListas(listagemListas);

        // Notificação de mudança ocorrida dos dados
        // utilizado pela RecyclerView vinculada ao
        // adapter para exibir os novos dados
        listasAdapter.notifyDataSetChanged();
    }
}
