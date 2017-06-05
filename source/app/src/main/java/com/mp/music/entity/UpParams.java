package com.mp.music.entity;

import com.mp.music.utils.FileUtils;

/**
 * Created by 张伟 on 2016-04-08.
 */
public class UpParams {

    private boolean fileTag;

    private String key;

    private String value;

    public UpParams(String key,String value){
        this.key=key;
        this.value=value;

        this.fileTag=FileUtils.isFilePath(value);
    }

    public boolean getFileTag() {
        return fileTag;
    }

    public void setFileTag(boolean fileTag) {
        this.fileTag = fileTag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
