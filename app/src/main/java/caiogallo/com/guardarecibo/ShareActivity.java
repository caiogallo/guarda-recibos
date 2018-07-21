package caiogallo.com.guardarecibo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class ShareActivity extends AppCompatActivity {
    public static final String TAG = "ShareActivity";
    public static final String MONTH_YEAR_TEMPLATE = "%s: %d";
    public static final String MONTH_YEAR_DIR_TEMPLATE = "GuardaRecibo/%d/%d";
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
            String saveFolder = makeFolder(getMonthOfYear(), getYear());
            Log.d(TAG, "make folder: " + saveFolder);

            InputStream inputStream = loadIntentFromStream(intent);
            saveStream(inputStream, saveFolder);
        }
    }

    private boolean canAccessExternalStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            Toast.makeText(this, "Não posso salvar recibos, não tenho permissão para isso :(", Toast.LENGTH_LONG);
            return false;
        }
    }

    private InputStream loadIntentFromStream(Intent intent) {

        if (intent.getExtras().containsKey(Intent.EXTRA_STREAM)) {
            List streams = (List) intent.getExtras().get(Intent.EXTRA_STREAM);
            ImageView bmpView = findViewById(R.id.bmpView);
            bmpView.setImageURI((Uri)streams.get(0));
            return getIntent((Uri) streams.get(0));
        }

        return null;

    }

    private void configureLabels() {
        setText(String.format(MONTH_YEAR_TEMPLATE, getResources().getString(R.string.lblMonth), getMonthOfYear()),
                (TextView) findViewById(R.id.txtMonth));
        setText(String.format(MONTH_YEAR_TEMPLATE, getResources().getString(R.string.lblYear), getYear()),
                (TextView) findViewById(R.id.txtYear));
    }

    private void setText(String msg, TextView editTextView){
        editTextView.setText(msg);
    }

    private Integer getMonthOfYear(){
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    private Integer getYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    private String makeFolder(int mes, int ano) {
        String dirpath = String.format(MONTH_YEAR_DIR_TEMPLATE, ano, mes);
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
            Toast.makeText(this, R.string.unableToSaveFile, Toast.LENGTH_LONG);
            Log.e(TAG, "File not found " + filename, e);
        } catch (IOException e) {
            Toast.makeText(this, R.string.unableToSaveFile, Toast.LENGTH_LONG);
            Log.e(TAG, "Can't read file ", e);
        }
    }


}
