package com.caiogallo.guardarecibo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.caiogallo.guardarecibo.utils.Constants;
import com.caiogallo.guardarecibo.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import caiogallo.com.guardarecibo.R;

public class ShareActivity extends AppCompatActivity {
    public static final String TAG = "ShareActivity";
    public static final String MONTH_YEAR_TEMPLATE = "%s: %d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Intent intent = getIntent();
        Log.i(TAG, String.format("intent type: %s", intent.getType()));
        Log.i(TAG, String.format("intent action: %s", intent.getAction()));

        configureLabels();

        boolean canAccessExternalStorage = canAccessExternalStorage();

        if (canAccessExternalStorage) {
            String saveFolder = makeFolder(Utils.getMonthOfYear(), Utils.getYear());
            Log.d(TAG, "make folder: " + saveFolder);

            InputStream inputStream = loadIntentFromStream(intent);
            saveStream(inputStream, saveFolder);
        }
    }

    private boolean canAccessExternalStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            Toast.makeText(this, R.string.unableToSaveFileNoPermission, Toast.LENGTH_LONG)
                    .show();
            return false;
        }
    }

    private InputStream loadIntentFromStream(Intent intent) {

        if (intent.getExtras().containsKey(Intent.EXTRA_STREAM)) {
            ImageView bmpView = findViewById(R.id.bmpView);
            Object extraStream = intent.getExtras().get(Intent.EXTRA_STREAM);
            if (extraStream instanceof List) {
                List streams = (List) extraStream;
                bmpView.setImageURI((Uri)streams.get(0));
                return getIntent((Uri) streams.get(0));
            }else{
                if (extraStream instanceof Uri) {
                    bmpView.setImageURI((Uri)extraStream);
                    return getIntent((Uri)extraStream);
                }
            }

        }

        return null;

    }

    private void configureLabels() {
        setText(String.format(MONTH_YEAR_TEMPLATE, getResources().getString(R.string.lblMonth), Utils.getMonthOfYear()),
                (TextView) findViewById(R.id.txtMonth));
        setText(String.format(MONTH_YEAR_TEMPLATE, getResources().getString(R.string.lblYear), Utils.getYear()),
                (TextView) findViewById(R.id.txtYear));
    }

    private void setText(String msg, TextView editTextView){
        editTextView.setText(msg);
    }

    private String makeFolder(int mes, int ano) {
        String dirpath = String.format(Constants.ROOT_DIR + Constants.MONTH_YEAR_DIR_TEMPLATE, ano, mes);
        File subdir = new File(Environment.getExternalStorageDirectory(), dirpath);
        if (subdir.exists()) {
            Log.i(TAG, String.format("path %s exists", dirpath));
            return subdir.toString();
        }
        try {
            if(subdir.mkdirs()){
                Log.d(TAG, String.format("path created %s", subdir));
                return subdir.toString();
            }else{
                Log.e(TAG, String.format("can't create %s", subdir));
            }
        } catch (SecurityException se) {
            Log.e(TAG, String.format("security exception trying to create %s", dirpath), se);
        }
        return null;
    }

    private InputStream getIntent(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            return inputStream;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "file not found", e);
        }
        return null;
    }

    public void saveStream(InputStream stream, String path) {
        String filename = path+ "/" + UUID.randomUUID().toString() + ".png";
        Log.d(TAG, "app filename " + filename);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);
            fileOutputStream.write(buffer);
            stream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, R.string.unableToSaveFile, Toast.LENGTH_LONG)
                    .show();
            Log.e(TAG, "File not found " + filename, e);
        } catch (IOException e) {
            Toast.makeText(this, R.string.unableToSaveFile, Toast.LENGTH_LONG)
                    .show();
            Log.e(TAG, "Can't read file ", e);
        }
    }


}
