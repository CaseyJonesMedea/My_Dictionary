package com.example.caseyjones.mydictionary.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.caseyjones.mydictionary.R;
import com.example.caseyjones.mydictionary.activities.OnFragmentInteractionListener;

/**
 * Created by CaseyJones on 22.05.2016.
 */
public class DeleteAccountDialog extends DialogFragment implements DialogInterface.OnClickListener{

    private View form = null;

    private OnFragmentInteractionListener onFragmentInteractionListener;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        onFragmentInteractionListener = (OnFragmentInteractionListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        form = getActivity().getLayoutInflater().inflate(R.layout.dialog_delete, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return (builder.setTitle("").setView(form)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel,null)
                .create());
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        onFragmentInteractionListener.deleteUser();
    }

    @Override
    public void onDismiss(DialogInterface unused) {
        super.onDismiss(unused);
    }
    @Override
    public void onCancel(DialogInterface unused) {
        super.onCancel(unused);
    }
}
