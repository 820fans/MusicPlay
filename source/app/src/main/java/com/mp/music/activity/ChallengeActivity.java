package com.mp.music.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.mp.music.R;
import com.mp.music.base.BaseActivity;
import com.mp.music.db.DatabaseManager;
import com.mp.music.entity.ChallengeDetail;
import com.mp.music.entity.Constants;
import com.mp.music.entity.NetResultData;
import com.mp.music.helper.ImageHelper;
import com.mp.music.helper.NetHelper;
import com.mp.music.utils.DialogUtils;
import com.mp.music.utils.StringUtils;
import com.mp.music.utils.TitleBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class ChallengeActivity extends BaseActivity {

	private ImageView avatar;
	private NetworkImageView imageA;
	private NetworkImageView imageB;
	private ScrollView scrollView;
	private LinearLayout input_bar;
	private RelativeLayout top_bar;
	private EditText answer;
	private Button submit;

	private TextView title;
	private TextView introduce;

	private static String aimTag=null;
	private static String aimMusic=null;

	private Timer timer=null;
	private TimerTask task=null;
	private TitleBuilder titleBuilder;

	private int challenge_id=-1;
	private String challenge_title="";
	private String acceptTime=null;
	private int num=30;

	Snackbar snackbar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challenge);

		titleBuilder=new TitleBuilder(this)
		.setLeftText("退出")
		.setTitleText("挑战")
        .setRightText("接受挑战")
		.setLeftOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//退出你画我猜
				doExitWhileTimer();
			}
		})
		.setRightOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (imageA.getVisibility() == View.VISIBLE) {
					showChallenge();
				}
			}
		});
		
		initView();

		initIntent();

		GetChallengeDetail();
	}

	public void initIntent(){

		ImageLoader.getInstance().displayImage(Constants.SERVER_URL + getIntent().getStringExtra("avatar"),
				avatar);

		challenge_title=getIntent().getStringExtra("title");
		// 特殊文字处理,将表情等转换一下
		title.setText(StringUtils.getWeiboContent(
				this, title, challenge_title));

		challenge_id=getIntent().getIntExtra("id",-1);
	}

	public void GetChallengeDetail(){
		//获取挑战详情
		NetHelper netHelper = new NetHelper(Constants.GET_CHALLENGE_DETAIL);
		netHelper.setParams("id",challenge_id+"");
		netHelper.setResultListener(new NetHelper.ResultListener() {

			@Override
			public void getResult(NetResultData result) {

				JSONArray detail = result.getData();

				try {

					//处理JSON数组数据
					JSONObject item = (JSONObject) detail.opt(0);

					ChallengeDetail challengeDetail = new ChallengeDetail(
							item.getInt("id"),
							item.getString("path_fog"),
							item.getString("path"),
							item.getString("account"),
							item.getString("avatar"),
							challenge_title,
							item.getString("introduce"),
							item.getString("tag"),
							item.getString("music")
					);

					//设置目的标签
					aimTag = item.getString("tag");
					aimMusic = item.getString("music");

					introduce.setText(challengeDetail.getIntroduce());
					imageA.setImageUrl(Constants.SERVER_URL + challengeDetail.getPath_fog(),
							ImageHelper.getLoader());
					imageB.setImageUrl(Constants.SERVER_URL + challengeDetail.getPath(),
							ImageHelper.getLoader());

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void getError() {
				Toast.makeText(ChallengeActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
			}
		});

		netHelper.doPost();
	}

	public void doExitWhileTimer(){

		timer=null;
		task=null;
		ChallengeActivity.this.finish();
	}


	public void confirmExit(){

		stopTime();
		if(num>0){
			DialogUtils.showConfirmDialog(ChallengeActivity.this, "提示", "时间尚未结束，确认退出？",
					"确认", "取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							timer = null;
							task = null;
							ChallengeActivity.this.finish();
						}
					}, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							startTime();
						}
					});
		}
	}

	@Override
	public void onBackPressed() {

		//还没接受挑战的时候
		if(num==30){
			doExitWhileTimer();
			return;
		}
		else{
			//在游戏结束之前退出
			confirmExit();
		}


	}

	/**
	 * 确认接受挑战
	 */
	public void showChallenge(){

		DialogUtils.showConfirmDialog(ChallengeActivity.this, "接收挑战", "确认接收挑战？" +
						"你将有30s的时间进行猜测",
				"确认", "取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						input_bar.setVisibility(View.VISIBLE);
						showImageB();
						acceptTime=RecordTime();
						startTime();
					}
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
	}

	public String RecordTime(){

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	/**
	 * 绘画结束 弹出框
	 */
	public void showUploadDialog(){

		if(timer!=null)
		DialogUtils.showConfirmDialog(ChallengeActivity.this, "结束", "时间已到，游戏结束",
				"确认", "", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						ChallengeActivity.this.finish();
					}
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
	}

	/**
	 * 你画我猜倒计时，UI更新
	 */
	private Handler mHanlder=new Handler(){
		public void handleMessage(Message msg) {
			num=msg.what;
			titleBuilder.setRightText("还剩下 " + num + " s");

			if(num==20){
				stopTime();
				DialogUtils.showConfirmDialog(ChallengeActivity.this, "挑战提示", "图片对应标签属于音乐：" + aimMusic,
						"确认", "", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								startTime();
							}
						}, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								startTime();
							}
						});
			}
			else if(num==10){
				stopTime();
				DialogUtils.showConfirmDialog(ChallengeActivity.this, "挑战提示", "标签的字数是："+aimTag.length(),
						"确认", "", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								startTime();
							}
						}, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								startTime();
							}
						});
			}
			else if(num<=0){
				stopTime();
				if(timer!=null)
				showUploadDialog();
			}
			else{
				startTime();
			}
		};
	};

	/**
	 * 倒计时开始
	 */
	public void startTime(){
		timer=new Timer();
		task=new TimerTask(){

			@Override
			public void run() {

				num--;
				Message message=mHanlder.obtainMessage();
				message.what=num;
				mHanlder.sendMessage(message);
			}
		};

		timer.schedule(task, 1000);
	}

	/**
	 * 倒计时暂停(结束)
	 */
	public void stopTime(){
		if(timer!=null)
		timer.cancel();
	}

	private void shwoImageA(){
		imageA.setVisibility(View.VISIBLE);
		imageB.setVisibility(View.INVISIBLE);
	}
	
	private void showImageB(){
		imageA.setVisibility(View.INVISIBLE);
		imageB.setVisibility(View.VISIBLE);
	}

	@SuppressWarnings("deprecation")
	private void initView(){
		title=(TextView) findViewById(R.id.challenge_title);
		avatar=(ImageView) findViewById(R.id.avatar);
		introduce=(TextView) findViewById(R.id.upper_introduce);
		imageA = (NetworkImageView) findViewById(R.id.ivA);
		imageB = (NetworkImageView) findViewById(R.id.ivB);
		imageA.setDefaultImageResId(R.drawable.appicon);

		scrollView=(ScrollView) findViewById(R.id.scroll_content);

		top_bar=(RelativeLayout) findViewById(R.id.imgs);

		input_bar=(LinearLayout) findViewById(R.id.input_controlbar);
		answer=(EditText)input_bar.findViewById(R.id.answer);
		submit=(Button)input_bar.findViewById(R.id.submit);
		input_bar.setVisibility(View.GONE);


		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				snackbar=Snackbar.make(top_bar, "测试弹出提示", Snackbar.LENGTH_SHORT);
				snackbar.setAction("取消", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						snackbar.dismiss();
					}
				});

				//答案
				String txt=answer.getText().toString();
				int flag=0;

				//猜中了
				if(txt.equals(aimTag)){

					((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
							.setTextColor(Color.parseColor("#00FF00"));
					snackbar.setText("恭喜你猜中了");
					flag=1;
				}
				else if(!txt.equals("")){
					((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
							.setTextColor(Color.parseColor("#FFFFFF"));
					snackbar.setText("很遗憾没有猜中");
				}

				if(txt.equals("")){
					((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
							.setTextColor(Color.parseColor("#FFFFFF"));
					snackbar.setText("请输入猜测内容");
					return;
				}

				snackbar.show();
				sendGuessData(txt,flag);
				answer.setText("");

			}
		});

		shwoImageA();
		
	}

	public void sendGuessData(String answer,int done){
		int time=num;//猜测的时间
		String user_id= DatabaseManager.getInstance(ChallengeActivity.this).getActiveUser();

		//上传猜测
		NetHelper netHelper = new NetHelper(Constants.UP_GUESS);
		netHelper.setParams("challenge_id",challenge_id+"");
		netHelper.setParams("user_id",user_id+"");
		netHelper.setParams("time",time +"");
		netHelper.setParams("answer",answer+"");
		netHelper.setParams("done",done+"");
		netHelper.setParams("acceptTime",acceptTime);
		netHelper.setResultListener(new NetHelper.ResultListener() {

			@Override
			public void getResult(NetResultData result) {

				JSONArray detail = result.getData();
			}

			@Override
			public void getError() {
				Toast.makeText(ChallengeActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
			}
		});

		netHelper.doPost();
	}
}
