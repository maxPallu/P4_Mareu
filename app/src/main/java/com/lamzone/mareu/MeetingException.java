package com.lamzone.mareu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.fragment.app.DialogFragment;

public class MeetingException extends Exception {

    private Context mContext;

    public MeetingException(Context context) {
        this.mContext = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Attention !")
                .setMessage("Vous ne pouvez pas créer de réunion vide !")
                .create()
                .show();
    }
}