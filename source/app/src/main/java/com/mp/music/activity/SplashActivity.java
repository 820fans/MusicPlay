package com.mp.music.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.Toast;

import com.mp.music.R;
import com.mp.music.base.BaseActivity;
import com.mp.music.db.DatabaseManager;
import com.mp.music.entity.Constants;
import com.mp.music.entity.NetResultData;
import com.mp.music.helper.NetHelper;


public class SplashActivity extends BaseActivity {
	
	private static final int WHAT_INTENT2LOGIN = 1;
	private static final int WHAT_INTENT2MAIN = 2;
	private static final long SPLASH_DUR_TIME = 1000;

	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case WHAT_INTENT2LOGIN:
				intent2Activity(LoginActivity.class);
				finish();
				break;
			case WHAT_INTENT2MAIN:
				intent2Activity(MainActivity.class);
				finish();
				break;
			default:
				break;
			}
		}
		
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		String imageUri = "drawable://" + R.drawable.splash;
		imageLoader.displayImage(imageUri,
				(ImageView) findViewById(R.id.iv_slogan));

		//获取存储的有效的user_id
		String user_id=DatabaseManager.getInstance(SplashActivity.this).getActiveUser();

		//没有有效的用户身份
		if(user_id.equals("")) {
			handler.sendEmptyMessageDelayed(WHAT_INTENT2LOGIN, SPLASH_DUR_TIME);
//			handler.sendEmptyMessageDelayed(WHAT_INTENT2MAIN, SPLASH_DUR_TIME);
			return;
		}

		//请求获取当前用户状态是否有效
		NetHelper netHelper = new NetHelper(Constants.IS_USER_ACTIVE);
		netHelper.setParams("user_id",user_id);
		netHelper.setResultListener(new NetHelper.ResultListener() {

			@Override
			public void getResult(NetResultData result) {

				int user = result.getCode();

				if(user==0){
					//身份已经失效
					handler.sendEmptyMessageDelayed(WHAT_INTENT2LOGIN, SPLASH_DUR_TIME);
				}
				else if(user==1){
					//身份有效，进入主界面
					handler.sendEmptyMessageDelayed(WHAT_INTENT2MAIN, SPLASH_DUR_TIME);
				}
			}

			@Override
			public void getError() {
				Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
			}
		});


				netHelper.doPost();
//		accessToken = AccessTokenKeeper.readAccessToken(this);
//		if(accessToken.isSessionValid()) {
//			handler.sendEmptyMessageDelayed(WHAT_INTENT2MAIN, SPLASH_DUR_TIME);
//		} else {
//			handler.sendEmptyMessageDelayed(WHAT_INTENT2LOGIN, SPLASH_DUR_TIME);
//		}
	}
}
