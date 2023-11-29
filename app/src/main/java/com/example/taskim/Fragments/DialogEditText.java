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
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
        void handleDialogClose(DialogInterface dialog);
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
                // Preenchimento da view com o layout do fragmento
                .setView(layoutInflater.inflate(R.layout.dialog_edit_text, null))
                // Callbacks do botão de sucesso e cancelar
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

        // Verifica se a atividade em questão implementa
        // a interface que lida com o fechamento do Dialog
        if (act instanceof DialogEditTextListener) {
            ((DialogEditTextListener) act).handleDialogClose(dialog);
        }
    }
}
