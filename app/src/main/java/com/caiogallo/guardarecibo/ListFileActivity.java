package com.caiogallo.guardarecibo;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.caiogallo.guardarecibo.filenavigator.FileNavigator;
import com.caiogallo.guardarecibo.filenavigator.FileNavigatorException;
import com.caiogallo.guardarecibo.listeners.ItemListClick;

import java.util.List;

import caiogallo.com.guardarecibo.R;

public class ListFileActivity extends ListActivity {
    public static final String TAG = "ListFileActivity";
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_file);

        path = getIntent().getStringExtra("path");
        FileNavigator fileNavigator = new FileNavigator();
        try {
            List values = fileNavigator.navigate(this, path);
            ArrayAdapter adapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_2, android.R.id.text1, values);
            setListAdapter(adapter);
        } catch (FileNavigatorException e) {
            Log.e(TAG, "Cannot navigate", e);
            Toast.makeText(this, String.format("Cannot navigate %s", e.getMessage()),
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        new ItemListClick(path, this, this).onItemClick(l, v, position, id);
    }
}
