package com.caiogallo.guardarecibo.filenavigator;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.caiogallo.guardarecibo.model.FileModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileNavigator {
    public static final String TAG = "FileNavigator";

    public List navigate(Context context, String rootFolder) throws FileNavigatorException {
        List<FileModel> values = new ArrayList();
        File dir = new File(rootFolder);
        if(!dir.canRead()){
            throw new FileNavigatorException(String.format("Não foi possível acessar a pasta %s", rootFolder));
        }
        List<String> list = Arrays.asList(dir.list());
        Collections.sort(list);
        if (list != null) {
            for (String strFile : list) {
                if (!strFile.startsWith(".")) {
                    File file = new File(rootFolder, strFile);
                    Log.i(TAG, String.format("file %s is directory %s", strFile, file.isDirectory()));
                    values.add(FileModel.builder()
                            .directory(file.isDirectory())
                            .name(file.getName())
                            .absolutPath(file.getAbsolutePath())
                            .build());
                }
            }
        }


        return values;
    }

}
