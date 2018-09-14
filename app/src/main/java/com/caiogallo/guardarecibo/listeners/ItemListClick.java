package com.caiogallo.guardarecibo.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.caiogallo.guardarecibo.ListFileActivity;

import java.io.File;

public class ItemListClick implements AdapterView.OnItemClickListener {
    public static final String TAG = "ItemListClick";

    private String path;
    private Activity activity;
    private Context context;

    public ItemListClick(String path, Activity activity, Context context) {
        this.path = path;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
        Log.i(TAG, "click item");
        String filename = (String) adapterView.getAdapter().getItem(pos);
        if (path.endsWith(File.separator)) {
            filename = path + filename;
        } else {
            filename = path + File.separator + filename;
        }
        if (new File(filename).isDirectory()) {
            Intent intent = new Intent(context, ListFileActivity.class);
            intent.putExtra("path", filename);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, filename + " is not a directory", Toast.LENGTH_LONG).show();
        }
    }
}
