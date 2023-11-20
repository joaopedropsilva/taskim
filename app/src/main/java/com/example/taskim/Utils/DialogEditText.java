package com.example.taskim.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.taskim.R;

    public class DialogEditText extends DialogFragment {
    public interface DialogEditTextListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    public static final String TAG = "DialogDialogEditText";
    private final String dialogTitle;
    private DialogEditTextListener listener;


    public DialogEditText(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    @Override
    public void onAttach(Context context) {
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

        builder.setTitle(dialogTitle)
                .setView(layoutInflater.inflate(R.layout.dialog_edit_text, null))
                .setPositiveButton("Adicionar", (dialog, which) -> {
                    listener.onDialogPositiveClick(DialogEditText.this);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    listener.onDialogNegativeClick(DialogEditText.this);
                });

        return builder.create();
    }
}
