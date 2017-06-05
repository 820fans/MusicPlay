package com.mp.music.listener;

/**
 * Created by 张伟 on 2016-04-09.
 */

/**
 * @ClassName: FreedomDataCallBack
 * @author victor_freedom (x_freedom_reddevil@126.com)
 * @createddate 2015-1-24 下午4:33:38
 * @Description: 回调接口，处理返回数据
 * @param <t>
 */
public interface DataCallBack<T> {

    void onStart();

    void processData(T paramObject, boolean paramBoolean);

    void onFinish();

    void onFailed();
}