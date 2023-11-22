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
import com.example.taskim.Utils.DialogEditText;

public class ListaRecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private final ListasAdapter listasAdapter;

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

    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
        final int listaIndex = viewHolder.getAdapterPosition();

        if (direction == ItemTouchHelper.LEFT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(listasAdapter.getContext());
            builder.setMessage("Deletar lista?");
            builder.setPositiveButton("Sim", (dialog, which) ->
                    listasAdapter.deleteListaByIndex(listaIndex));
            builder.setNegativeButton("Não", (dialog, which) ->
                    listasAdapter.notifyItemChanged(viewHolder.getAdapterPosition()));

            AlertDialog dialogDeleteLista = builder.create();
            dialogDeleteLista.show();
        } else {
            Lista lista = listasAdapter.getListagemListas().get(listaIndex);
            Bundle listaInformartion = new Bundle();
            listaInformartion.putInt("id", lista.getId());
            listaInformartion.putString("nome", lista.getNome());
            listaInformartion.putString("operation", "edit");

            DialogEditText dialogEditLista = new DialogEditText(
                    "Editar nome da lista",
                     "Salvar",
                    "Cancelar");
            dialogEditLista.setArguments(listaInformartion);

            ListagemListasActivity activity = (ListagemListasActivity) listasAdapter.getContext();
            dialogEditLista.show(activity.getSupportFragmentManager(), DialogEditText.TAG);
        }
    }

    @Override
    public void onChildDraw(
            Canvas canvas,
            RecyclerView recyclerView,
            RecyclerView.ViewHolder viewHolder,
            float dX,
            float dY,
            int actionState,
            boolean isCurrentlyActive
    ) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable swipeIcon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if (dX > 0) {
            swipeIcon = ContextCompat.getDrawable(listasAdapter.getContext(), R.drawable.edit_icon);
            background = new ColorDrawable(ContextCompat.getColor(listasAdapter.getContext(), R.color.colorPrimary));
        } else {
            swipeIcon = ContextCompat.getDrawable(listasAdapter.getContext(), R.drawable.delete_icon);
            background = new ColorDrawable(Color.RED);
        }

            int swipeIconMargin = (itemView.getHeight() - swipeIcon.getIntrinsicHeight()) / 2;
        int swipeIconTop = itemView.getTop() + (itemView.getHeight() - swipeIcon.getIntrinsicHeight()) / 2;
        int swipeIconBottom = swipeIconTop + swipeIcon.getIntrinsicHeight();

        if (dX > 0) {
            int swipeIconLeft = itemView.getLeft() + swipeIconMargin;
            int swipeIconRight = itemView.getLeft() + swipeIconMargin + swipeIcon.getIntrinsicHeight();

            swipeIcon.setBounds(swipeIconLeft, swipeIconTop, swipeIconRight, swipeIconBottom);
            background.setBounds(
                    itemView.getLeft(),
                    itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());
        } else if (dX < 0) {
            int swipeIconLeft = itemView.getRight() - swipeIconMargin - swipeIcon.getIntrinsicWidth();
            int swipeIconRight = itemView.getRight() - swipeIconMargin;

            swipeIcon.setBounds(swipeIconLeft, swipeIconTop, swipeIconRight, swipeIconBottom);
            background.setBounds(
                    itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(),
                    itemView.getRight(),
                    itemView.getBottom());
        } else {
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(canvas);
        swipeIcon.draw(canvas);
    }
}
