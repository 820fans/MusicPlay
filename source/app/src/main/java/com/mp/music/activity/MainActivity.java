package com.mp.music.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.mp.music.R;
import com.mp.music.entity.Constants;
import com.mp.music.fragment.FragmentController;


public class MainActivity extends FragmentActivity implements
		OnCheckedChangeListener  {
	
	private RadioGroup rg_tab;
	private FragmentController controller;

	ExitReceiver mReceiver=new ExitReceiver();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		controller=new FragmentController(this, R.id.fl_content);
		
		controller.showFragment(0);
		
		initView();

		//注册接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.EXIT_ACTION);
		registerReceiver(mReceiver, filter);
	}
	
	private void initView() {
		rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
		
		rg_tab.setOnCheckedChangeListener(this);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_home:
			controller.showFragment(0);
			break;
		case R.id.rb_message:
			controller.showFragment(1);
			break;
		case R.id.rb_search:
			controller.showFragment(2);
			break;
		case R.id.rb_user:
			controller.showFragment(3);
			break;
		default:
			break;
		}
	}

	class ExitReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			unregisterReceiver(this); // 这句话必须要写要不会报错，不写虽然能关闭，会报一堆错
			MainActivity.this.finish();
		}
	}
}
