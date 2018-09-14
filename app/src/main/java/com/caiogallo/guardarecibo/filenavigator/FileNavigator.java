package com.caiogallo.guardarecibo.filenavigator;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileNavigator {
    public static final String TAG = "FileNavigator";

    public List navigate(Context context, String rootFolder) throws FileNavigatorException {
        List values = new ArrayList<>();
        File dir = new File(rootFolder);
        if(!dir.canRead()){
            throw new FileNavigatorException(String.format("Não foi possível acessar a pasta %s", rootFolder));
        }
        String[] list = dir.list();
        Log.i(TAG, "file list");
        for (String file : list) {
            Log.i(TAG, file);
        }
        if (list != null) {
            for (String file : list) {
                if (!file.startsWith(".")) {
                    values.add(file);
                }
            }
        }

        Collections.sort(values);
        return values;
    }
}
