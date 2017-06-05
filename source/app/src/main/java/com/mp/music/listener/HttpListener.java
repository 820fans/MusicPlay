package com.mp.music.listener;

/**
 * Created by 张伟 on 2016-04-09.
 */
public interface HttpListener {

    int EVENT_BASE = 0x100;

    /**
     * 没有网络的信息提示
     * */
    int EVENT_NOT_NETWORD = EVENT_BASE + 1;

    /**
     * 网络异常的信息提示
     * */
    int EVENT_NETWORD_EEEOR = EVENT_BASE + 2;

    /**
     * 获取网络数据失败
     * */
    int EVENT_GET_DATA_EEEOR = EVENT_BASE + 3;

    /**
     * 获取网络数据成功
     * */
    int EVENT_GET_DATA_SUCCESS = EVENT_BASE + 4;
    /**
     * 获取网络数据成功
     * */
    int EVENT_CLOSE_SOCKET = EVENT_BASE + 5;

    void action(int actionCode, Object object);
}