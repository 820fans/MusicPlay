package com.mp.music.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mp.music.utils.DialogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BaseFragment extends Fragment {

	protected Dialog progressDialog;;
	
	protected ImageLoader imageLoader;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		progressDialog = DialogUtils.createLoadingDialog(getActivity());
		
		imageLoader = ImageLoader.getInstance();
	}
	
	protected void intent2Activity(Class<? extends Activity> tarActivity) {
		Intent intent = new Intent(getActivity(), tarActivity);
		startActivity(intent);
	}

}
