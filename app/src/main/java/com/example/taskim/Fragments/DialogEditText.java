package com.example.taskim.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.taskim.R;

    public class DialogEditText extends DialogFragment {
    public interface DialogEditTextListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
        public void handleDialogClose(DialogInterface dialog);
    }
    public static final String TAG = "DialogDialogEditText";
    private final String title;
    private final String positiveClickText;
    private final String negativeClickText;
    private DialogEditTextListener listener;

    public DialogEditText(String title, String positiveClickText, String negativeClickText) {
        this.title = title;
        this.positiveClickText = positiveClickText;
        this.negativeClickText = negativeClickText;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogEditTextListener) context;
        } catch (ClassCastException error) {
            throw new ClassCastException(getActivity().toString() +
                    " precisa implementar a interface DialogEditTextListener");
        }
    }
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();

        builder.setTitle(title)
                .setView(layoutInflater.inflate(R.layout.dialog_edit_text, null))
                .setPositiveButton(positiveClickText, (dialog, which) -> {
                    listener.onDialogPositiveClick(DialogEditText.this);
                })
                .setNegativeButton(negativeClickText, (dialog, which) -> {
                    listener.onDialogNegativeClick(DialogEditText.this);
                });

        return builder.create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity act = getActivity();

        // Verifica se a atividade em questão implementa a interface que lida com
        // o fechamento do Dialog de AddTarefa, se for o caso, chama o método que
        // atualiza a RecyclerView com a nova tarefa
        if (act instanceof DialogEditText.DialogEditTextListener) {
            ((DialogEditText.DialogEditTextListener) act).handleDialogClose(dialog);
        }
    }
}
