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

public class ListImageTitleAdapter extends BaseAdapter{
    public static final String TAG = "ListImageTitleAdapter";

    private List<FileModel> items;
    private Activity activity;

    public ListImageTitleAdapter(List<FileModel> items, Activity activity) {
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
        View view1 = activity.getLayoutInflater().inflate(R.layout.list_image_title, viewGroup, false);
        FileModel fileModel = items.get(i);

        createImageView(view1, fileModel);
        createTextView(view1, fileModel);
        return view1;
    }

    private void createTextView(View view1, FileModel fileModel) {
        TextView textView = view1.findViewById(R.id.txt_comprovante);
        textView.setText(fileModel.getName());
    }

    private void createImageView(View view1, FileModel fileModel) {
        ImageView imgView = view1.findViewById(R.id.img_comprovante);
        imgView.setImageBitmap(loadFromPath(fileModel.getAbsolutPath()));
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imgView.setColorFilter(filter);
    }

    private Bitmap loadFromPath(final String filename) {
        File imgfile = new File(filename);
        if(imgfile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgfile.getAbsolutePath());
            return bitmap;
        }else{
            Log.e(TAG, String.format("file %s not fount", filename));
        }
        return null;
    }
}
