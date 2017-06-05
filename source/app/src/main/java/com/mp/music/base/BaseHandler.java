package com.mp.music.base;

import android.os.Handler;
import android.os.Message;

import com.mp.music.listener.DataCallBack;
import com.mp.music.listener.HttpListener;

/**
 * Created by 张伟 on 2016-04-09.
 */
public class BaseHandler extends Handler {
    /** 事件回调接口处理 */
    private DataCallBack callBack;

    public BaseHandler(DataCallBack callBack) {

        this.callBack = callBack;
    }

    public void handleMessage(Message msg) {
        // 根据不同的结果触发不同的动作
        if (msg.what == HttpListener.EVENT_GET_DATA_SUCCESS) {
            if (msg.obj == null) {
                callBack.onFailed();
            } else {
                // 后台处理数据
                callBack.processData(msg.obj, true);
            }
        } else if (msg.what == HttpListener.EVENT_NOT_NETWORD) {
            callBack.onFailed();
            // CommonUtil.showInfoDialog(context,
            // getString(R.string.net_error));
        } else if (msg.what == HttpListener.EVENT_NETWORD_EEEOR) {
            callBack.onFailed();

        } else if (msg.what == HttpListener.EVENT_GET_DATA_EEEOR) {
            callBack.onFailed();

        } else if (msg.what == HttpListener.EVENT_CLOSE_SOCKET) {

        }
        callBack.onFinish();
    }

}