package com.mp.music.helper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mp.music.base.BaseApplication;
import com.mp.music.entity.NetResultData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetHelper {

    private static RequestQueue queue = BaseApplication.netQueue;
    private ResultListener listener;

    private String url;
    private HashMap<String, String> map;

    public NetHelper(String url) {
        this.url = url;
        map = new HashMap<>();
    }

    public void setParams(String key, String value) {
        map.put(key, value);
    }

    public void doPost() {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.println(response);

                try {
                    JSONObject json = new JSONObject(response);
                    NetResultData result = new NetResultData();
                    result.setRaw(response);
                    result.setCode(json.optInt("code"));
                    result.setMsg(json.optString("msg"));
                    result.setData(json.optJSONArray("data"));
                    listener.getResult(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.getError();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                listener.getError();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                return map;
            }
        };

        queue.add(request);
    }

    public interface ResultListener {
        void getResult(NetResultData result);
        void getError();
    }

    public void setResultListener(ResultListener listener) {
        this.listener = listener;
    }
}
