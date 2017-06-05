package com.mp.music.entity;

import org.json.JSONArray;

/**
 * Created by 张伟 on 2016-04-04.
 */
public class NetResultData {

    private int code;

    private String msg;

    private String raw;

    private JSONArray data;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public JSONArray getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }
}
