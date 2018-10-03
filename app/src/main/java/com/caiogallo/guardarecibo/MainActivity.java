package com.caiogallo.guardarecibo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.caiogallo.guardarecibo.filenavigator.FileNavigator;
import com.caiogallo.guardarecibo.filenavigator.FileNavigatorException;
import com.caiogallo.guardarecibo.listeners.ItemListClick;
import com.caiogallo.guardarecibo.utils.Constants;

import java.io.File;
import java.util.List;

import caiogallo.com.guardarecibo.R;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_PERMISSION_STORAGE = 1;
    public static final String TAG = "MainActivity";
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        verifyStoragePermission(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestStoragePermission();
        navigate();
    }

    private void verifyStoragePermission(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            for (String permission : permissions) {
                if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE) ||
                        permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.i(TAG, permission + " grant");
                        permissionVisibility(View.INVISIBLE);
                    }else{
                        Log.i(TAG, permission + " revoke");
                        permissionVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    public void btnRequestPermissionOnClick(View btnRequestPermission) {
        requestStoragePermission();
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
            permissionVisibility(View.INVISIBLE);
        }
    }

    private void permissionVisibility(final int visibility) {
        View btnPedirPermissao = findViewById(R.id.btnPedirPermissao);
        if (btnPedirPermissao != null) {
            btnPedirPermissao.setVisibility(visibility);
        }
        View txtPedirPermissao = findViewById(R.id.txtPedirPermissao);
        if (txtPedirPermissao != null) {
            txtPedirPermissao.setVisibility(visibility);
        }
    }

    private void navigate() {
        File appPath = Environment.getExternalStorageDirectory();
        String appPathStr = appPath.getPath() + File.separator + Constants.ROOT_DIR;
        FileNavigator navigator = new FileNavigator();
        try {
            List navigate = navigator.navigate(this, appPathStr, 1);
            ArrayAdapter adapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, navigate);
            ListView listaArquivosRaiz = findViewById(R.id.lista_arquivos_raiz);
            listaArquivosRaiz.setAdapter(adapter);
            listaArquivosRaiz.setOnItemClickListener(new ItemListClick(appPathStr, this, this));
        } catch (FileNavigatorException e) {
            Log.e(TAG, "Cannot navigate", e);
            Toast.makeText(this, String.format("Cannot navigate %s", e.getMessage()),
                    Toast.LENGTH_LONG).show();
        }
    }


}
