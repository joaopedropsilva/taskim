package com.example.taskim.Helpers;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskim.Adapters.ListasAdapter;
import com.example.taskim.Dados.Lista;
import com.example.taskim.ListagemListasActivity;
import com.example.taskim.R;
import com.example.taskim.Fragments.DialogEditText;


// Classe de helper de ItemTouchHelper.SimpleCallback
// responsável pelas as ações de deslizar para os lados
// um item do tipo lista de tarefas
public class ListaRecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private final ListasAdapter listasAdapter;

    // Definição do construtor com o adapter
    // responsável pelas listas de tarefas
    public ListaRecyclerItemTouchHelper(ListasAdapter listasAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.listasAdapter = listasAdapter;
    }

    @Override
    public boolean onMove(
            @NonNull RecyclerView rvListagemListas,
            @NonNull RecyclerView.ViewHolder viewHolder,
            @NonNull RecyclerView.ViewHolder target
    ) { return false; }

    // Implementação da rotina de execução quando um usuário
    // arrasta uma lista de tarefas para algum lado
    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
        // Recuperação da posição da lista de tarefas em questão
        final int listaIndex = viewHolder.getAdapterPosition();

        // Ação tomada ao arrastar para a
        // esquerda: abertura de um Dialog
        // para a deleção do item em questão
        if (direction == ItemTouchHelper.LEFT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(listasAdapter.getContext());
            builder.setMessage("Deletar lista?");
            builder.setPositiveButton("Sim", (dialog, which) ->
                    listasAdapter.deleteListaByIndex(listaIndex));
            builder.setNegativeButton("Não", (dialog, which) ->
                    listasAdapter.notifyItemChanged(viewHolder.getAdapterPosition()));

            AlertDialog dialogDeleteLista = builder.create();
            dialogDeleteLista.show();
        }
        // Ação tomada ao arrastar para a
        // direita: abertura do DialogEditText
        // para a edição do item em questão
        else {
            // Busca pela lista em questão na listagem
            Lista lista = listasAdapter.getListagemListas().get(listaIndex);

            // Criação de um bundle com as informações
            // da operação de edição
            Bundle listaInformartion = new Bundle();
            listaInformartion.putInt("id", lista.getId());
            listaInformartion.putString("nome", lista.getNome());
            listaInformartion.putString("operation", "edit");

            // Criação do DialogEditText de edição
            DialogEditText dialogEditLista = new DialogEditText(
                    "Editar nome da lista",
                     "Salvar",
                    "Cancelar");

            // Passagem dos argumentos para o dialog
            dialogEditLista.setArguments(listaInformartion);

            // Recuperação da atividade em questão
            ListagemListasActivity activity = (ListagemListasActivity) listasAdapter.getContext();

            // Função de abertura do Dialog de edição
            dialogEditLista.show(activity.getSupportFragmentManager(), DialogEditText.TAG);
        }
    }

    // Função responsável pelo comportamento da View
    // de lista de tarefas quando ela é arrastada
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
                    listasAdapter.getContext(), R.drawable.edit_icon);
            background = new ColorDrawable(
                    ContextCompat.getColor(listasAdapter.getContext(), R.color.colorPrimary));
        }
        // Caso contrário é exibido um ícone
        // de deleção do item em questão
        // com o fundo em vermelho
        else {
            swipeIcon = ContextCompat.getDrawable(
                    listasAdapter.getContext(), R.drawable.delete_icon);
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
