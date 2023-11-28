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
import com.example.taskim.Utils.DialogEditText;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Collections;
import java.util.List;

public class ListagemListasActivity extends AppCompatActivity
        implements DialogEditText.DialogEditTextListener {
    private ListasAdapter listasAdapter;
    private List<Lista> listagemListas;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView rvListagemListas;
        ExtendedFloatingActionButton btnAddLista;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.listagem_listas);

        db = new Database(this);
        db.openDb();

        listasAdapter = new ListasAdapter(this, db);

        rvListagemListas = findViewById(R.id.rvListagemListas);
        rvListagemListas.setLayoutManager(new LinearLayoutManager(this));
        rvListagemListas.setAdapter(listasAdapter);

        btnAddLista = findViewById(R.id.btnAddLista);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ListaRecyclerItemTouchHelper(listasAdapter));
        itemTouchHelper.attachToRecyclerView(rvListagemListas);


        listagemListas = db.searchAllListas();
        // Inverte a ordem da lista para exibir a última lista
        // adicionada em primeiro lugar
        Collections.reverse(listagemListas);

        listasAdapter.setListagemListas(listagemListas);

        btnAddLista.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("operation", "add");

            DialogEditText dialog = new DialogEditText(
                    "Adicionar nova lista",
                    "Adicionar",
                    "Cancelar");
            dialog.setArguments(args);

            dialog.show(getSupportFragmentManager(), DialogEditText.TAG);
        });
    }

    @Override
    public void onResumeFragments() {
        super.onResumeFragments();

        FragmentManager frgManager = getSupportFragmentManager();
        Fragment fragment = frgManager.findFragmentByTag(DialogEditText.TAG);

        if (fragment != null) {
            frgManager.beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        String operation = dialog.getArguments().getString("operation");
        int idLista = dialog.getArguments().getInt("id");
        EditText edtDialogLista = dialog.getDialog().findViewById(R.id.edtInput);

        if (edtDialogLista != null) {
            String nomeLista = edtDialogLista.getText().toString();

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

        // Esconde o Dialog
        dialog.getDialog().dismiss();
    }
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {}

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        listagemListas = db.searchAllListas();
        // Inverte a ordem da lista para exibir a última tarefa
        // adicionada em primeiro lugar
        Collections.reverse(listagemListas);

        listasAdapter.setListagemListas(listagemListas);
        listasAdapter.notifyDataSetChanged();
    }
}
