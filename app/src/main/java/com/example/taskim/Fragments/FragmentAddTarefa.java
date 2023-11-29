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
import com.example.taskim.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FragmentAddTarefa extends BottomSheetDialogFragment {
    public interface FragmentAddTarefaListener {
        void handleDialogClose(DialogInterface dialog);
    }
    public static final String TAG = "FragmentAddTarefa";
    private EditText edtNovaTarefa;
    private Button btnNovaTarefa;
    private Database db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Estabelecimento da estilização desejada no fragmento
        setStyle(STYLE_NORMAL, R.style.AddTarefaStyle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup viewGroup, Bundle savedInstanceState) {
        // Preenchimento da view com o layout do fragmento
        View view = inflater.inflate(R.layout.nova_tarefa, viewGroup, false);

        // Permite que o fragmento ajuste seu tamanho de acordo com o input do usuário
        getDialog().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Recuperação dos recursos do layout utilizados em tela
        edtNovaTarefa = getView().findViewById(R.id.edtNovaTarefa);
        btnNovaTarefa = getView().findViewById(R.id.btnNovaTarefa);

        // Instanciação e abertura do banco de dados
        db = new Database(getActivity());
        db.openDb();

        // Verificação de operação
        // de atualização ou inserção
        // de uma nova tarefa
        boolean isUpdateTarefa = false;

        // Recuperação de argumentos
        // passados ao fragmento
        final Bundle bundleData = getArguments();

        // Verificação se os argumentos são nulos
        if (bundleData != null) {
            // Caso não sejam, o conteúdo da tarefa é recuperado
            String conteudo = bundleData.getString("conteudo");

            if (conteudo != null) {
                // Se o conteúdo não é nulo,
                // trata-se de uma atualização da
                // tarefa em questão
                isUpdateTarefa = true;

                // Conteúdo é passado para o EditText
                edtNovaTarefa.setText(conteudo);

                // Caso o EditText contenha alguma coisa digitada
                // o botão de adicionar troca para a cor primária
                if (conteudo.length() > 0) {
                    btnNovaTarefa.setTextColor(
                            ContextCompat.getColor(requireContext(),
                            R.color.colorPrimary));
                }
            }
        }

        // Event listeners do EditText
        edtNovaTarefa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Caso não haja nada no EditText o botão de
                // adicionar é desativado e sua cor é cinza
                if (s.toString().equals("")) {
                    btnNovaTarefa.setEnabled(false);
                    btnNovaTarefa.setTextColor(Color.GRAY);
                }
                // Caso contrário, o botão de adicionar
                // é ativo e sua cor é a primária
                else {
                    btnNovaTarefa.setEnabled(true);
                    btnNovaTarefa.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Redeclaração de variável para uso no
        // callback do botão de adição/edição de tarefa
        boolean finalIsUpdateTarefa = isUpdateTarefa;

        // Listener para o clique no botão de adição/edição de tarefa
        btnNovaTarefa.setOnClickListener(v -> {
            // Recuperação do conteúdo digitado
            String conteudo = edtNovaTarefa.getText().toString();

            // Se for uma atualização de tarefa
            // chamda o update no Database handler
            if (finalIsUpdateTarefa) {
                db.udpateConteudoTarefa(bundleData.getInt("id"), conteudo);
            }
            // Caso contrário trata-se de uma inserção
            else {
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
        if (act instanceof FragmentAddTarefaListener) {
            ((FragmentAddTarefaListener) act).handleDialogClose(dialog);
        }
    }
}
