package com.mp.music.base;


import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import com.mp.music.entity.UpParams;
import com.mp.music.file.UploadFile;
import com.mp.music.listener.DataCallBack;
import com.mp.music.listener.HttpListener;
import com.mp.music.utils.DialogUtils;
import com.mp.music.utils.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public abstract class BaseActivity extends Activity {

	protected BaseApplication application;
	protected String TAG;

	private CommonRecevier commonRecevier;
	protected SharedPreferences sp;
	protected Intent intent;
	protected Dialog progressDialog;

	protected ImageLoader imageLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TAG = this.getClass().getSimpleName();
		showLog("onCreate()");
		
		application = (BaseApplication) getApplication();
		sp = getSharedPreferences("sp_music", MODE_PRIVATE);
		intent = getIntent();
		progressDialog = DialogUtils.createLoadingDialog(this);
		application.addActivity(this);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		imageLoader = ImageLoader.getInstance();
	}

	/**
	 * 将文件和键值对上传到服务器
	 * @param context 当前Context
	 * @param targetURL 需要传输到的目的URL
	 * @param list 需要传输的键值对，其中如果是文件，则value为文件的路径，key为自定(如img等)
	 * @param callBack 回调函数
	 */
	protected void putDatatoServer(Context context,String targetURL,
								   ArrayList<UpParams> list,
								   DataCallBack callBack) {
		final BaseHandler handler = new BaseHandler(callBack);
		UploadFile uploadFile = new UploadFile(context,
				new HttpListener() {

					@Override
					public void action(int actionCode, Object object) {

						Message msg = new Message();
						switch (actionCode) {
							case HttpListener.EVENT_NOT_NETWORD:
								msg.what = HttpListener.EVENT_NOT_NETWORD;
								break;

							case HttpListener.EVENT_NETWORD_EEEOR:
								msg.what = HttpListener.EVENT_NETWORD_EEEOR;
								break;
							case HttpListener.EVENT_CLOSE_SOCKET:
								msg.what = HttpListener.EVENT_CLOSE_SOCKET;
								break;

							case HttpListener.EVENT_GET_DATA_EEEOR:
								msg.what = HttpListener.EVENT_GET_DATA_EEEOR;
								msg.obj = null;
								break;
							case HttpListener.EVENT_GET_DATA_SUCCESS:
								msg.obj = object;
								msg.what = HttpListener.EVENT_GET_DATA_SUCCESS;
								break;
							default:
								break;
						}
						handler.sendMessage(msg);
					}
				}, targetURL,list);

		callBack.onStart();
		uploadFile.postRequest();

	}

	protected void intent2Activity(Class<?> tarActivity) {
		Intent intent = new Intent(this, tarActivity);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		showLog("onResume()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		showLog("onDestroy()");
		
//		application.removeActivity(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		showLog("onPause()");
	}

	protected void finishActivity() {
		this.finish();
	}

	protected void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	protected void showLog(String msg) {
		Logger.show(TAG, msg);
	}

	@Override
	protected void onStart() {
		super.onStart();
//		registerReceiver(commonRecevier, new IntentFilter(BROADCASTRECEVIER_ACTON));
	}


	@Override
	protected void onStop() {
		super.onStop();
//		unregisterReceiver(commonRecevier);
	}


	public class CommonRecevier extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	}
}
