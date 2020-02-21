package com.caiogallo.guardarecibo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.caiogallo.guardarecibo.adapters.ListFolderAdapter;
import com.caiogallo.guardarecibo.filenavigator.FileNavigator;
import com.caiogallo.guardarecibo.filenavigator.FileNavigatorException;
import com.caiogallo.guardarecibo.listeners.ItemListClick;
import com.caiogallo.guardarecibo.utils.Constants;

import java.io.File;
import java.util.ArrayList;
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
        View btnRequestPermission = findViewById(R.id.btnPedirPermissao);
        if (btnRequestPermission != null) {
            btnRequestPermission.setVisibility(visibility);
        }
        View txtRequestPermission = findViewById(R.id.txtPedirPermissao);
        if (txtRequestPermission != null) {
            txtRequestPermission.setVisibility(visibility);
        }
    }

    public List getFolders(String appPathStr){
        FileNavigator navigator = new FileNavigator();
        try {
            List navigate = navigator.navigate(this, appPathStr, 1);
            return navigate;
        }catch (FileNavigatorException e) {
            Log.e(TAG, "Cannot navigate", e);
            Toast.makeText(this, String.format("Cannot navigate %s", e.getMessage()),
                    Toast.LENGTH_LONG).show();
        }
        return new ArrayList();
    }

    private void navigate() {
        File appPath = Environment.getExternalStorageDirectory();
        String appPathStr = appPath.getPath() + File.separator + Constants.ROOT_DIR;
        List folders = getFolders(appPathStr);
        ListView listRootFiles = findViewById(R.id.lista_arquivos_raiz);
        listRootFiles.setEmptyView(findViewById(R.id.emptyListView));
        ListFolderAdapter adapter = new ListFolderAdapter(folders, this);
        listRootFiles.setAdapter(adapter);
        listRootFiles.setOnItemClickListener(
                new ItemListClick(appPathStr, this, this));
    }


}
