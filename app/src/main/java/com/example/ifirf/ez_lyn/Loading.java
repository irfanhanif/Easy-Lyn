package com.example.ifirf.ez_lyn;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by ifirf on 5/16/2017.
 */

public class Loading {
    private Context myContext;
    private AlertDialog dialog;
    private String TAG = "Loading";

    public Loading (Context context){
        myContext = context;
        dialog = null;
    }

    public void displayLoading(){
        Log.d(TAG, "Loading displayed");
        final AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        final LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogContent = inflater.inflate(R.layout.loading, null);

        builder.setView(dialogContent);

        dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
    }

    public void hideLoading(){
        if(dialog != null){
            Log.d(TAG, "Loading dismiss");
            dialog.dismiss();
        }
    }
}
