package com.example.taskim;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskim.Adapters.ListasAdapter;
import com.example.taskim.Dados.Lista;
import com.example.taskim.Handlers.Database;
import com.example.taskim.Handlers.DialogCloseListener;
import com.example.taskim.Helpers.ListaRecyclerItemTouchHelper;
import com.example.taskim.Utils.DialogEditText;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Collections;
import java.util.List;

public class ListagemListasActivity extends AppCompatActivity implements DialogEditText.DialogEditTextListener {
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
            DialogEditText dialog = new DialogEditText("Adicionar nova lista");
            dialog.show(getSupportFragmentManager(), DialogEditText.TAG);
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogLista) {
        EditText edtDialogLista = dialogLista.getDialog().findViewById(R.id.edtInput);

        if (edtDialogLista != null) {
            String nomeLista = edtDialogLista.getText().toString();

            listasAdapter.addLista(nomeLista);
        }
    }
    @Override
    public void onDialogNegativeClick(DialogFragment dialogLista) {}
}
