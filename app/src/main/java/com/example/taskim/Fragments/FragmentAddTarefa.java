package com.example.taskim.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.taskim.Dados.Tarefa;
import com.example.taskim.Handlers.Database;
import com.example.taskim.Handlers.DialogCloseListener;
import com.example.taskim.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FragmentAddTarefa extends BottomSheetDialogFragment {
    public static final String TAG = "FragmentAddTarefa";
    private EditText edtNovaTarefa;
    private Button btnNovaTarefa;
    private Database db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NORMAL, R.style.AddTarefaStyle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nova_tarefa, viewGroup, false);

        // Permite que o fragmento ajuste seu tamanho de acordo com o input do usuário
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtNovaTarefa = getView().findViewById(R.id.edtNovaTarefa);
        btnNovaTarefa = getView().findViewById(R.id.btnNovaTarefa);

        db = new Database(getActivity());
        db.openDb();

        boolean isUpdateTarefa = false;
        final Bundle bundleData = getArguments();

        if (bundleData != null) {
            String conteudo = bundleData.getString("conteudo");

            if (conteudo != null) {
                isUpdateTarefa = true;
                edtNovaTarefa.setText(conteudo);

                if (conteudo.length() > 0) {
                    btnNovaTarefa.setTextColor(
                            ContextCompat.getColor(requireContext(),
                            R.color.colorPrimary));
                }
            }
        }

        edtNovaTarefa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    btnNovaTarefa.setEnabled(false);
                    btnNovaTarefa.setTextColor(Color.GRAY);
                } else {
                    btnNovaTarefa.setEnabled(true);
                    btnNovaTarefa.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        boolean finalIsUpdateTarefa = isUpdateTarefa;
        btnNovaTarefa.setOnClickListener(v -> {
            String conteudo = edtNovaTarefa.getText().toString();

            if (finalIsUpdateTarefa) {
                db.udpateConteudoTarefa(bundleData.getInt("id"), conteudo);
            } else {
                int idLista = bundleData.getInt("id_lista");

                Tarefa t = new Tarefa();
                t.setConteudo(conteudo);
                t.setStatus(false);

                db.insertTarefa(t, idLista);
            }

            // Esconde o Dialog
            dismiss();
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity act = getActivity();

        // Verifica se a atividade em questão implementa a interface que lida com
        // o fechamento do Dialog de AddTarefa, se for o caso, chama o método que
        // atualiza a RecyclerView com a nova tarefa
        if (act instanceof DialogCloseListener) {
            ((DialogCloseListener) act).handleDialogClose(dialog);
        }
    }
}
