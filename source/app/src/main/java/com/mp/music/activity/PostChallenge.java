package com.mp.music.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mp.music.R;
import com.mp.music.adapter.EmotionGvAdapter;
import com.mp.music.adapter.EmotionPagerAdapter;
import com.mp.music.adapter.WriteStatusGridImgsAdapter;
import com.mp.music.base.BaseActivity;
import com.mp.music.db.DatabaseManager;
import com.mp.music.entity.Constants;
import com.mp.music.entity.Emotion;
import com.mp.music.entity.UpParams;
import com.mp.music.listener.DataCallBack;
import com.mp.music.utils.DialogUtils;
import com.mp.music.utils.DisplayUtils;
import com.mp.music.utils.ImageUtils;
import com.mp.music.utils.StringUtils;
import com.mp.music.utils.TitleBuilder;
import com.mp.music.views.WrapHeightGridView;

import java.util.ArrayList;
import java.util.List;

public class PostChallenge  extends BaseActivity implements OnClickListener, OnItemClickListener {

	// 输入框
	private EditText et_write_status;
	// 添加的九宫格图片
	private WrapHeightGridView gv_write_status;
	// 底部添加栏
	private ImageView iv_image;
	private ImageView iv_at;
	private ImageView iv_topic;
	private ImageView iv_emoji;
	private ImageView iv_add;
	// 表情选择面板
	private LinearLayout ll_emotion_dashboard;
	private ViewPager vp_emotion_dashboard;
	// 进度框
	private ProgressDialog progressDialog;

	private WriteStatusGridImgsAdapter statusImgsAdapter;

	private ArrayList<Uri> imgUris = new ArrayList<Uri>();
	private EmotionPagerAdapter emotionPagerGvAdapter;
	int tagId=-1;
	private AlertDialog mUploadMsg;
	String type;//表示需要发布的类型

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_challenge_post);
		
		initView();
		
		//添加画好的图片
		type=intent.getStringExtra("type");
		if(type.equals("challenge")) {
			Uri uri = getIntent().getData();
			imgUris.add(uri);
		}
		else if(type.equals("talk")){
			((TextView)findViewById(R.id.write_title)).setVisibility(View.VISIBLE);
		}

		//音乐标签的id
		//或者 论坛分区的id
		tagId = getIntent().getIntExtra("id", -1);
	}


	private void initView() {
		// 标题栏
		new TitleBuilder(this)
				.setTitleText("发布页面")
				.setLeftText("取消")
				.setLeftOnClickListener(this)
				.setRightText("发布")
				.setRightOnClickListener(this)
				.build();
		// 输入框
		et_write_status = (EditText) findViewById(R.id.et_write_status);
		// 添加的九宫格图片
		gv_write_status = (WrapHeightGridView) findViewById(R.id.gv_write_status);

		// 底部添加栏
		iv_image = (ImageView) findViewById(R.id.iv_image);
		iv_emoji = (ImageView) findViewById(R.id.iv_emoji);

		// 表情选择面板
		ll_emotion_dashboard = (LinearLayout) findViewById(R.id.ll_emotion_dashboard);
		vp_emotion_dashboard = (ViewPager) findViewById(R.id.vp_emotion_dashboard);
		// 进度框
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("发布中...");

		statusImgsAdapter = new WriteStatusGridImgsAdapter(this, imgUris, gv_write_status);
		gv_write_status.setAdapter(statusImgsAdapter);
//		gv_write_status.setOnItemClickListener(this);

//		iv_image.setOnClickListener(this);
//		iv_at.setOnClickListener(this);
//		iv_topic.setOnClickListener(this);
		iv_emoji.setOnClickListener(this);
//		iv_add.setOnClickListener(this);

		initEmotion();

		mUploadMsg = new AlertDialog.Builder(PostChallenge.this)
				.setTitle("提示")
				.setMessage("成功上传")
				.setPositiveButton("确认返回", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						PostChallenge.this.finish();

					}
				})
				.create();
	}

	public ArrayList<UpParams> CollectDatas(){
		ArrayList<UpParams> list=new ArrayList<>();

		UpParams upParams=new UpParams("id",tagId+"");
		list.add(upParams);

		UpParams user_id=new UpParams("user_id",
				DatabaseManager.getInstance(PostChallenge.this).getActiveUser());
		list.add(user_id);

		UpParams txt=new UpParams("content",et_write_status.getText().toString());
		list.add(txt);

		Uri uri = imgUris.get(0);
		String imgFilePath = ImageUtils.getImageAbsolutePath(this, uri);
		UpParams img=new UpParams("img",imgFilePath);
		list.add(img);

		return  list;
	}

	/**
	 * 发送微博
	 */
	private void sendStatus() {
		String comment = et_write_status.getText().toString();
		if(TextUtils.isEmpty(comment)) {
			showToast("文字内容不能为空");
			return;
		}
		if(type.equals("talk") &&
				TextUtils.isEmpty(((TextView) findViewById(R.id.write_title)).getText().toString())){
			showToast("请填写标题内容");
			return;
		}

		String UP_URL;
		ArrayList<UpParams> list=new ArrayList<>();
		if(getIntent().getStringExtra("type").equals("challenge")){
			UP_URL=Constants.UP_CHALLENGE;
			list=CollectDatas();
		}
		else{
			UP_URL=Constants.UP_TALK;

			UpParams upParams=new UpParams("id",tagId+"");
			list.add(upParams);

			UpParams user_id=new UpParams("user_id",
					DatabaseManager.getInstance(PostChallenge.this).getActiveUser());
			list.add(user_id);

			UpParams title=new UpParams("title",((TextView) findViewById(R.id.write_title))
					.getText().toString());
			list.add(title);

			UpParams txt=new UpParams("content",et_write_status.getText().toString());
			list.add(txt);
		}

		putDatatoServer(PostChallenge.this,UP_URL,
				list,new DataCallBack<String>() {

			@Override
			public void onStart() {
				progressDialog.show();
			}

			@Override
			public void processData(String paramObject, boolean paramBoolean) {

			}

			@Override
			public void onFinish() {
				progressDialog.dismiss();
				mUploadMsg.show();
			}

			@Override
			public void onFailed() {

			}
		});

	}

	/**
	 *  初始化表情面板内容
	 */
	private void initEmotion() {
		// 获取屏幕宽度
		int gvWidth = DisplayUtils.getScreenWidthPixels(this);
		// 表情边距
		int spacing = DisplayUtils.dp2px(this, 8);
		// GridView中item的宽度
		int itemWidth = (gvWidth - spacing * 8) / 7;
		int gvHeight = itemWidth * 3 + spacing * 4;

		List<GridView> gvs = new ArrayList<GridView>();
		List<String> emotionNames = new ArrayList<String>();
		// 遍历所有的表情名字
		for (String emojiName : Emotion.emojiMap.keySet()) {
			emotionNames.add(emojiName);
			// 每20个表情作为一组,同时添加到ViewPager对应的view集合中
			if (emotionNames.size() == 20) {
				GridView gv = createEmotionGridView(emotionNames, gvWidth, spacing, itemWidth, gvHeight);
				gvs.add(gv);
				// 添加完一组表情,重新创建一个表情名字集合
				emotionNames = new ArrayList<String>();
			}
		}

		// 检查最后是否有不足20个表情的剩余情况
		if (emotionNames.size() > 0) {
			GridView gv = createEmotionGridView(emotionNames, gvWidth, spacing, itemWidth, gvHeight);
			gvs.add(gv);
		}

		// 将多个GridView添加显示到ViewPager中
		emotionPagerGvAdapter = new EmotionPagerAdapter(gvs);
		vp_emotion_dashboard.setAdapter(emotionPagerGvAdapter);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gvWidth, gvHeight);
		vp_emotion_dashboard.setLayoutParams(params);
	}

	/**
	 * 创建显示表情的GridView
	 */
	private GridView createEmotionGridView(List<String> emotionNames, int gvWidth, int padding, int itemWidth, int gvHeight) {
		// 创建GridView
		GridView gv = new GridView(this);
		gv.setBackgroundResource(R.color.bg_gray);
		gv.setSelector(R.color.transparent);
		gv.setNumColumns(7);
		gv.setPadding(padding, padding, padding, padding);
		gv.setHorizontalSpacing(padding);
		gv.setVerticalSpacing(padding);
		LayoutParams params = new LayoutParams(gvWidth, gvHeight);
		gv.setLayoutParams(params);
		// 给GridView设置表情图片
		EmotionGvAdapter adapter = new EmotionGvAdapter(this, emotionNames, itemWidth);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(this);
		return gv;
	}

	/**
	 * 更新图片显示
	 */
	private void updateImgs() {
		if(imgUris.size() > 0) {
			// 如果有图片则显示GridView,同时更新内容
			gv_write_status.setVisibility(View.VISIBLE);
			statusImgsAdapter.notifyDataSetChanged();
		} else {
			// 无图则不显示GridView
			gv_write_status.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebar_tv_left:
			finish();
			break;
		case R.id.titlebar_tv_right:
			sendStatus();
			break;
		case R.id.iv_image:
			DialogUtils.showImagePickDialog(this);
			break;
		case R.id.iv_emoji:
			if(ll_emotion_dashboard.getVisibility() == View.VISIBLE) {
				// 显示表情面板时点击,将按钮图片设为笑脸按钮,同时隐藏面板
				iv_emoji.setImageResource(R.drawable.btn_insert_emotion);
				ll_emotion_dashboard.setVisibility(View.GONE);
			} else {
				// 未显示表情面板时点击,将按钮图片设为键盘,同时显示面板
				iv_emoji.setImageResource(R.drawable.btn_insert_keyboard);
				ll_emotion_dashboard.setVisibility(View.VISIBLE);
			}
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Object itemAdapter = parent.getAdapter();

		if (itemAdapter instanceof WriteStatusGridImgsAdapter) {
			// 点击的是添加的图片
			if (position == statusImgsAdapter.getCount() - 1) {
				// 如果点击了最后一个加号图标,则显示选择图片对话框
				DialogUtils.showImagePickDialog(this);
			}
//			else{
//				showIfNeedEditDialog(statusImgsAdapter.getItem(0));
//			}
		} else if (itemAdapter instanceof EmotionGvAdapter) {
			// 点击的是表情
			EmotionGvAdapter emotionGvAdapter = (EmotionGvAdapter) itemAdapter;

			if (position == emotionGvAdapter.getCount() - 1) {
				// 如果点击了最后一个回退按钮,则调用删除键事件
				et_write_status.dispatchKeyEvent(new KeyEvent(
						KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
			} else {
				// 如果点击了表情,则添加到输入框中
				String emotionName = emotionGvAdapter.getItem(position);

				// 获取当前光标位置,在指定位置上添加表情图片文本
				int curPosition = et_write_status.getSelectionStart();
				StringBuilder sb = new StringBuilder(et_write_status.getText().toString());
				sb.insert(curPosition, emotionName);

				// 特殊文字处理,将表情等转换一下
				et_write_status.setText(StringUtils.getWeiboContent(
						this, et_write_status, sb.toString()));
				
				// 将光标设置到新增完表情的右侧
				et_write_status.setSelection(curPosition + emotionName.length());
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO 自动生成的方法存根
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case ImageUtils.GET_IMAGE_BY_CAMERA:
			if(resultCode == RESULT_CANCELED) {
				// 如果拍照取消,将之前新增的图片地址删除
				ImageUtils.deleteImageUri(this, ImageUtils.imageUriFromCamera);
			} else {
//				// 拍照后将图片添加到页面上
//				imgUris.add(ImageUtils.imageUriFromCamera);
//				updateImgs();
				
				// crop
				ImageUtils.cropImage(this, ImageUtils.imageUriFromCamera);
			}
		case ImageUtils.CROP_IMAGE:
			if(resultCode != RESULT_CANCELED) {
				imgUris.add(ImageUtils.cropImageUri);
				updateImgs();
			}
			break;
		case ImageUtils.GET_IMAGE_FROM_PHONE:
			if(resultCode != RESULT_CANCELED) {
				// 本地相册选择完后将图片添加到页面上
				imgUris.add(data.getData());
				updateImgs();
			}
			break;
		default:
			break;
		}
	}

}