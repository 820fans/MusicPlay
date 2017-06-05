package com.mp.music.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mp.music.R;
import com.mp.music.activity.AboutActivity;
import com.mp.music.activity.EditPasswordActivity;
import com.mp.music.activity.LevelActivity;
import com.mp.music.activity.SettingActivity;
import com.mp.music.adapter.UserItem;
import com.mp.music.adapter.UserItemAdapter;
import com.mp.music.base.BaseFragment;
import com.mp.music.db.DatabaseManager;
import com.mp.music.entity.Constants;
import com.mp.music.entity.NetResultData;
import com.mp.music.entity.User;
import com.mp.music.helper.NetHelper;
import com.mp.music.utils.TitleBuilder;
import com.mp.music.views.WrapHeightListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class User2Fragment extends BaseFragment {

	private LinearLayout ll_userinfo;
	
	private ImageView iv_avatar;
	private TextView tv_account;
	private TextView tv_introduce;

	private WrapHeightListView lv_user_items;

	private View view;
	private User user;
	private ProgressBar levelProgress;
	private TextView levelInt;

	private UserItemAdapter adapter;
	private List<UserItem> userItems;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.frag_user2, null);
		
		initView();
		setItem();

		return view;
	}

	private void initView() {
		new TitleBuilder(view)
			.setTitleText("我")
			.build();

		//点击用户头像进入用户空间
//		ll_userinfo = (LinearLayout) view.findViewById(R.id.ll_userinfo);
//		ll_userinfo.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
////				Intent intent = new Intent(getActivity(), UserInfoActivity.class);
////				startActivity(intent);
//			}
//		});

		//用户头像、账户、简介信息
		iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
		tv_account = (TextView) view.findViewById(R.id.tv_account);
		tv_introduce = (TextView) view.findViewById(R.id.tv_introduce);

		levelInt=(TextView)view.findViewById(R.id.levelInt);
		levelProgress=(ProgressBar)view.findViewById(R.id.levelProgress);

		lv_user_items = (WrapHeightListView) view.findViewById(R.id.lv_user_items);
		userItems = new ArrayList<UserItem>();
		adapter = new UserItemAdapter(getActivity(), userItems);
		lv_user_items.setAdapter(adapter);

		adapter.setOnItemClickLitener(new UserItemAdapter.OnItemClickLitener() {
			@Override
			public void onItemClick(View view, int position) {

				System.out.println(position);

				//修改密码
				if(position==0){
					intent2Activity(EditPasswordActivity.class);
				}

				//等级信息
				if(position==1){
					Intent intent =new Intent();
					intent.setClass(getActivity(), LevelActivity.class);
					intent.putExtra("avatar", user.getAvatar());
					intent.putExtra("brand", user.getBrand());
					intent.putExtra("exp", user.getExp());
					intent.putExtra("level",user.getLevel());
					intent.putExtra("needexp",user.getNeedexp()+user.getLevelexp()-user.getExp());
					startActivity(intent);

				}

				//设置
				if(position==2){
					intent2Activity(SettingActivity.class);
				}
				//关于我们
				if(position==3){
					intent2Activity(AboutActivity.class);
				}

			}
		});

		//获取用户信息
		GetUserData();
	}

	@Override
	public void onResume() {
		super.onResume();
		GetUserData();
	}

	public void GetUserData(){
		//获取用户信息
		NetHelper netHelper = new NetHelper(Constants.GET_USERINFO);
		netHelper.setParams("user_id", DatabaseManager.getInstance(getActivity()).getActiveUser());
		netHelper.setResultListener(new NetHelper.ResultListener() {

			@Override
			public void getResult(NetResultData result) {

				JSONArray musicList = result.getData();

				try {

					JSONObject item = (JSONObject) musicList.opt(0);

					user=new User(
							item.getInt("id"),
							item.getString("account"),
							item.getString("avatar"),
							item.getString("introduce"),
							item.getString("brand"),
							item.getInt("exp"),
							item.getInt("needexp"),
							item.getInt("levelexp"),
							item.getInt("level")
					);

					setUserInfo();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void getError() {
				Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
			}
		});

		netHelper.doPost();
	}

	private void setItem() {
		userItems.add(new UserItem(false, R.drawable.push_icon_app_small_1, "修改密码", ""));
//		userItems.add(new UserItem(false, R.drawable.push_icon_app_small_2, "关于我的挑战", ""));
//		userItems.add(new UserItem(false, R.drawable.push_icon_app_small_3, "关于我的帖子", ""));
		userItems.add(new UserItem(false, R.drawable.push_icon_app_small_4, "等级信息", ""));
		userItems.add(new UserItem(false, R.drawable.push_icon_app_small_3, "设置", ""));
		userItems.add(new UserItem(true, R.drawable.push_icon_app_small_5, "关于我们", ""));

		adapter.notifyDataSetChanged();
	}

	private void setUserInfo() {

		// set data
//		tv_account.setText(user.getAccount());
//		tv_introduce.setText("简介:" + user.getIntroduce());
		imageLoader.displayImage(Constants.SERVER_URL+user.getAvatar(), iv_avatar);
		tv_account.setText(user.getAccount());
		tv_introduce.setText(user.getIntroduce());

		//加载等级信息
		levelInt.setText("LV " + user.getLevel());
		levelProgress.setMax(user.getNeedexp());
		levelProgress.setProgress(user.getExp()-user.getLevelexp());

	}


}
