package com.caiogallo.guardarecibo.model;

import lombok.Builder;
import lombok.Getter;import lombok.ToString;

@Getter
@Builder
public class FileModel {
    private String name;
    private String absolutPath;
    private boolean directory;
    private int depth;

    @Override
    public String toString() {
        return name;
    }
}
