package com.example.taskim.Handlers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskim.Adapters.TarefaAdapter;
import com.example.taskim.R;

public class TarefaRecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private TarefaAdapter tarefaAdapter;

    public TarefaRecyclerItemTouchHelper(TarefaAdapter tarefaAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.tarefaAdapter = tarefaAdapter;
    }

    @Override
    public boolean onMove(RecyclerView rvListaTarefa, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        final int tarefaIndex = viewHolder.getAdapterPosition();

        if (direction == ItemTouchHelper.LEFT) {
            AlertDialog.Builder confirmDialogBuilder = new AlertDialog.Builder(tarefaAdapter.getContext());
            confirmDialogBuilder.setMessage("Deletar tarefa?");
            confirmDialogBuilder.setPositiveButton("Sim", (dialog, which) ->
                    tarefaAdapter.deleteTarefaByIndex(tarefaIndex));
            confirmDialogBuilder.setNegativeButton("Não", (dialog, which) ->
                    tarefaAdapter.notifyItemChanged(viewHolder.getAdapterPosition()));

            AlertDialog confirmDialog = confirmDialogBuilder.create();
            confirmDialog.show();
        } else {
            tarefaAdapter.editTarefaByIndex(tarefaIndex);
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
            swipeIcon = ContextCompat.getDrawable(tarefaAdapter.getContext(), R.drawable.edit_icon);
            background = new ColorDrawable(ContextCompat.getColor(tarefaAdapter.getContext(), R.color.colorPrimary));
        } else {
            swipeIcon = ContextCompat.getDrawable(tarefaAdapter.getContext(), R.drawable.delete_icon);
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
