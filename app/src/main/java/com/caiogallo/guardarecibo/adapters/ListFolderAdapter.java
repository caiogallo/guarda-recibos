package com.caiogallo.guardarecibo.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.caiogallo.guardarecibo.model.FileModel;

import java.io.File;
import java.util.List;

import caiogallo.com.guardarecibo.R;

public class ListFolderAdapter extends BaseAdapter{
    public static final String TAG = "ListFolderAdapter";

    private List<FileModel> items;
    private Activity activity;

    public ListFolderAdapter(List<FileModel> items, Activity activity) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = activity.getLayoutInflater().inflate(R.layout.list_folders, viewGroup, false);
        FileModel fileModel = items.get(i);

        createTextView(view1, fileModel);
        return view1;
    }

    private void createTextView(View view1, FileModel fileModel) {
        TextView textView = view1.findViewById(R.id.txt_folder);
        textView.setText(fileModel.getName());
    }
}
