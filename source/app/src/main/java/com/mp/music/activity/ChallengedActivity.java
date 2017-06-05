package com.mp.music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.mp.music.R;
import com.mp.music.adapter.GuessTimeAdapter;
import com.mp.music.base.BaseActivity;
import com.mp.music.constants.GuessTimeItem;
import com.mp.music.db.DatabaseManager;
import com.mp.music.entity.Constants;
import com.mp.music.entity.NetResultData;
import com.mp.music.helper.ImageHelper;
import com.mp.music.helper.NetHelper;
import com.mp.music.utils.StringUtils;
import com.mp.music.utils.TitleBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ChallengedActivity extends BaseActivity {

	private NetworkImageView imageA;
	private TextView title;

	private TitleBuilder titleBuilder;
	private RecyclerView mRecyclerView;
	private GuessTimeAdapter mAdapter;
	private ArrayList<GuessTimeItem> mDatas=new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challenged);

		titleBuilder=new TitleBuilder(this)
		.setLeftImage(R.drawable.back)
		.setTitleText("挑战记录")
		.setLeftOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//退出你画我猜
				ChallengedActivity.this.finish();
			}
		});

		initView();

		final String challenge_title=getIntent().getStringExtra("title");
		// 特殊文字处理,将表情等转换一下
		title.setText(StringUtils.getWeiboContent(
				this, title, challenge_title));


		//得到控件
		mRecyclerView = (RecyclerView)this.findViewById(R.id.recycle_guesses);
		mRecyclerView.setHasFixedSize(true);
		//设置布局管理器
		//设置布局管理器
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChallengedActivity.this);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(linearLayoutManager);
		//设置适配器
		mAdapter = new GuessTimeAdapter(this, mDatas);

		mAdapter.setOnItemClickLitener(new GuessTimeAdapter.OnItemClickLitener() {
			@Override
			public void onItemClick(View view, int position) {

				Intent intent = new Intent();
				intent.setClass(ChallengedActivity.this,GuessActivity.class);
				intent.putExtra("time", mDatas.get(position).getTime());
				intent.putExtra("id", getIntent().getIntExtra("id", -1));
//				System.out.println("Clikc:"+getIntent().getIntExtra("id", -1));
				startActivity(intent);
			}
		});

		mRecyclerView.setAdapter(mAdapter);

		//获取猜测详情
		NetHelper netHelper = new NetHelper(Constants.GET_GUESSED);
		netHelper.setParams("id",getIntent().getIntExtra("id",-1)+"");
		netHelper.setParams("user_id", DatabaseManager.getInstance(
				ChallengedActivity.this).getActiveUser()+"");
		netHelper.setResultListener(new NetHelper.ResultListener() {

			@Override
			public void getResult(NetResultData result) {

				JSONArray detail = result.getData();

				try {

					//处理JSON数组数据
					JSONObject item = (JSONObject) detail.opt(0);

					imageA.setImageUrl(Constants.SERVER_URL+item.getString("path"),
							ImageHelper.getLoader());

					for(int i=1;i<detail.length();i++){
						JSONObject item1 = (JSONObject) detail.opt(i);
						GuessTimeItem guessTimeItem=new GuessTimeItem(
								item1.getString("acceptTime"));
						mDatas.add(guessTimeItem);
					}

					((TextView)findViewById(R.id.time_count)).setText((detail.length()-1)+"");

					mAdapter.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void getError() {
				Toast.makeText(ChallengedActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
			}
		});

		netHelper.doPost();
	}

	@SuppressWarnings("deprecation")
	private void initView(){
		title=(TextView) findViewById(R.id.challenge_title);
		imageA = (NetworkImageView) findViewById(R.id.ivA);
		imageA.setDefaultImageResId(R.drawable.appicon);
		
	}
}
