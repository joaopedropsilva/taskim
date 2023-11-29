package com.example.taskim.Helpers;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskim.Adapters.TarefaAdapter;
import com.example.taskim.R;

// Classe de helper de ItemTouchHelper.SimpleCallback
// responsável pelas as ações de deslizar para os lados
// um item do tipo tarefa
public class TarefaRecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private final TarefaAdapter tarefaAdapter;

    // Definição do construtor com o adapter
    // responsável pelas tarefas
    public TarefaRecyclerItemTouchHelper(TarefaAdapter tarefaAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.tarefaAdapter = tarefaAdapter;
    }

    @Override
    public boolean onMove(
            @NonNull RecyclerView rvListaTarefa,
            @NonNull RecyclerView.ViewHolder viewHolder,
            @NonNull RecyclerView.ViewHolder target
    ) { return false; }

    // Implementação da rotina de execução quando um
    // usuário arrasta uma tarefa para algum lado
    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
        // Recuperação da posição da tarefa em questão
        final int tarefaIndex = viewHolder.getAdapterPosition();

        // Ação tomada ao arrastar para a
        // esquerda: abertura de um Dialog
        // para a deleção do item em questão
        if (direction == ItemTouchHelper.LEFT) {
            AlertDialog.Builder confirmDialogBuilder = new AlertDialog.Builder(tarefaAdapter.getContext());
            confirmDialogBuilder.setMessage("Deletar tarefa?");
            confirmDialogBuilder.setPositiveButton("Sim", (dialog, which) ->
                    tarefaAdapter.deleteTarefaByIndex(tarefaIndex));
            confirmDialogBuilder.setNegativeButton("Não", (dialog, which) ->
                    tarefaAdapter.notifyItemChanged(viewHolder.getAdapterPosition()));

            AlertDialog confirmDialog = confirmDialogBuilder.create();
            confirmDialog.show();
        }
        // Ação tomada ao arrastar para a
        // direita: chamada da função de edição
        // de tarefa do adapter que abre o
        // fragmento de edição de tarefa
        else {
            tarefaAdapter.editTarefaByIndex(tarefaIndex);
        }
    }

    // Função responsável pelo comportamento
    // da View de tarefa quando ela é arrastada
    @Override
    public void onChildDraw(
            @NonNull Canvas canvas,
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder,
            float dX,
            float dY,
            int actionState,
            boolean isCurrentlyActive
    ) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        // Declaração de recursos utilizados
        // na estilização dos elementos em
        // tela durante a ação
        Drawable swipeIcon;
        ColorDrawable background;

        // Recuperação da view em qestão
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        // Deslocamento horizontal causado
        // pelo usuário é maior que zero, se
        // sim: exibição de um ícone de edição
        // com a cor principal como fundo
        if (dX > 0) {
            swipeIcon = ContextCompat.getDrawable(
                    tarefaAdapter.getContext(), R.drawable.edit_icon);
            background = new ColorDrawable(
                    ContextCompat.getColor(tarefaAdapter.getContext(), R.color.colorPrimary));
        }
        // Caso contrário é exibido um ícone
        // de deleção do item em questão
        // com o fundo em vermelho
        else {
            swipeIcon = ContextCompat.getDrawable(
                    tarefaAdapter.getContext(), R.drawable.delete_icon);
            background = new ColorDrawable(Color.RED);
        }

        // Definição de variáveis de posicionamento do ícone
        int swipeIconMargin = (itemView.getHeight() - swipeIcon.getIntrinsicHeight()) / 2;
        int swipeIconTop = itemView.getTop() +
                (itemView.getHeight() - swipeIcon.getIntrinsicHeight()) / 2;
        int swipeIconBottom = swipeIconTop + swipeIcon.getIntrinsicHeight();

        // Deslocamento horizontal causado
        // pelo usuário é maior que zero, se
        // sim: definição de determinada aparência,
        // posicionamento e limites para ícone/fundo
        if (dX > 0) {
            int swipeIconLeft = itemView.getLeft() + swipeIconMargin;
            int swipeIconRight = itemView.getLeft() + swipeIconMargin + swipeIcon.getIntrinsicHeight();

            swipeIcon.setBounds(swipeIconLeft, swipeIconTop, swipeIconRight, swipeIconBottom);
            background.setBounds(
                    itemView.getLeft(),
                    itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());
        }
        // Deslocamento horizontal causado
        // pelo usuário é menor que zero, se
        // sim: definição de outra aparência,
        // posicionamento e limites para ícone/fundo
        else if (dX < 0) {
            int swipeIconLeft = itemView.getRight() - swipeIconMargin - swipeIcon.getIntrinsicWidth();
            int swipeIconRight = itemView.getRight() - swipeIconMargin;

            swipeIcon.setBounds(swipeIconLeft, swipeIconTop, swipeIconRight, swipeIconBottom);
            background.setBounds(
                    itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(),
                    itemView.getRight(),
                    itemView.getBottom());
        }
        // Deslocamento horizontal causado
        // pelo usuário é igual a zero, se
        // sim: definição do fundo
        // com dimensões nulas
        else {
            background.setBounds(0, 0, 0, 0);
        }

        // Desenho do ícone e fundo
        // configurados na tela
        background.draw(canvas);
        swipeIcon.draw(canvas);
    }
}
