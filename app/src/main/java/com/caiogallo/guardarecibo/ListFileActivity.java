package com.caiogallo.guardarecibo;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.caiogallo.guardarecibo.adapters.ListImageTitleAdapter;
import com.caiogallo.guardarecibo.filenavigator.FileNavigator;
import com.caiogallo.guardarecibo.filenavigator.FileNavigatorException;
import com.caiogallo.guardarecibo.listeners.ItemListClick;
import com.caiogallo.guardarecibo.model.FileModel;

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
        List<FileModel> values = null;
        try {
            values = fileNavigator.navigate(this, path);
        } catch (FileNavigatorException e) {
            Log.e(TAG, "Cannot navigate", e);
            Toast.makeText(this, String.format("Cannot navigate %s", e.getMessage()),
                    Toast.LENGTH_LONG).show();
        }
        boolean isDirectory = isDirectoryStructure(values);
        Log.i(TAG, String.format("first %s is directory %s", values.get(0), isDirectory));
        if (isDirectory) {
            ArrayAdapter adapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);
            setListAdapter(adapter);
        }else{
            ListImageTitleAdapter adapter = new ListImageTitleAdapter(values, this);
            setListAdapter(adapter);
        }
    }
    private boolean isDirectoryStructure(List<FileModel> values) {
        if (values != null) {
            return values.stream().filter(f -> f.isDirectory()).count() > 0;
        }
        return false;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        new ItemListClick(path, this, this).onItemClick(l, v, position, id);
    }
}