package com.mp.music.activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mp.music.R;
import com.mp.music.base.BaseActivity;
import com.mp.music.constants.Doodle;
import com.mp.music.utils.DialogUtils;
import com.mp.music.utils.TitleBuilder;
import com.mp.music.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

;
;


public class DoodleActivity extends BaseActivity implements OnClickListener {

	private Doodle mDoodle;

	private AlertDialog mColorDialog;
	private AlertDialog mPaintDialog;
	private AlertDialog mShapeDialog;

	private TextView tag;
	private TextView clock_txt;
	private int tagId;


	private Timer timer=null;
	private TimerTask task=null;
	int num=30;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doodle);

		new TitleBuilder(this)
		.setLeftText("退出")
		.setTitleText("你画我猜")
		.setRightText("提交")
		.setLeftOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//退出你画我猜
				confirmExit();
			}
		})
		.setRightOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				DialogUtils.showConfirmDialog(DoodleActivity.this, "提示", "时间尚未结束，确认提交？",
						"确认", "取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								timer.cancel();
								submitChallenge();
							}
						}, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								startTime();
							}
						});

			}
		});

		mDoodle = (Doodle) findViewById(R.id.doodle_surfaceview);
		mDoodle.setSize(dip2px(5));

		tag=(TextView) findViewById(R.id.music_tag);
		clock_txt=(TextView) findViewById(R.id.clock_txt);

		//设置标签
		tag.setText(getIntent().getStringExtra("tag"));
		tagId=getIntent().getIntExtra("id",-1);

		findViewById(R.id.color_picker).setOnClickListener(this);
		findViewById(R.id.paint_picker).setOnClickListener(this);
		findViewById(R.id.eraser_picker).setOnClickListener(this);
		findViewById(R.id.shape_picker).setOnClickListener(this);

		startTime();
	}

	/**
	 * 保存图片到内部存储(外部图片管理器等不能看到)
	 * @param bitmapImage
	 * @return
	 */
	private String saveToInternalStorage(Bitmap bitmapImage){
		ContextWrapper cw = new ContextWrapper(DoodleActivity.this);
		// path to /data/data/yourapp/app_data/imageDir
		File directory = cw.getDir("challengeDir", Context.MODE_PRIVATE);
		// Create imageDir
		File imgFile=new File(directory,System.currentTimeMillis()+".png");

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(imgFile);
			// Use the compress method on the BitMap object to write image to the OutputStream
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return imgFile.getAbsolutePath();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mDoodle.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.color_picker:
			showColorDialog();
			
			break;
		case R.id.paint_picker:
			showSizeDialog();
			break;
		case R.id.eraser_picker:
			mDoodle.setType(Doodle.ActionType.Path);
			mDoodle.setSize(dip2px(20));
			mDoodle.setColor("#ffffff");
			
			ToastUtils.showToast(DoodleActivity.this, "切换为橡皮~~", Toast.LENGTH_SHORT);
			break;
		case R.id.shape_picker:
			showShapeDialog();
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		confirmExit();
	}

	public void confirmExit(){

		timer.cancel();

		if(num>0){
		DialogUtils.showConfirmDialog(DoodleActivity.this, "提示", "时间尚未结束，确认退出？",
				"确认", "取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						timer=null;
						task=null;
						DoodleActivity.this.finish();
					}
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startTime();
					}
				});
		}
	}

	/**
	 * 提交挑战
	 */
	public void submitChallenge(){

		Intent intent = new Intent(DoodleActivity.this, PostChallenge.class);
		String path = saveToInternalStorage(mDoodle.getBitmap());
		Uri tag = Uri.fromFile(new File(path));
		intent.setData(tag);
		intent.putExtra("type","challenge");
		intent.putExtra("id", tagId);
		startActivity(intent);

		DoodleActivity.this.finish();
	}

	/**
	 * 绘画结束 弹出框
	 */
	public void showUploadDialog(){

		DialogUtils.showConfirmDialog(DoodleActivity.this, "提交", "时间已到，请确认是否要提交",
				"我要发布", "取消退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						submitChallenge();
					}
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DoodleActivity.this.finish();
					}
				});
	}
	
	/**
	 * 你画我猜倒计时
	 */
	private Handler mHanlder=new Handler(){
		public void handleMessage(Message msg) {

			clock_txt.setText(msg.what+"");
			if(msg.what>0){
				startTime();
			}
			else {
				stopTime();
				showUploadDialog();
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
	
	public void stopTime(){
		timer.cancel();
	}

	/**
	 * 显示形状选择
	 */
	private void showShapeDialog() {
		if (mShapeDialog == null) {
			mShapeDialog = new AlertDialog.Builder(this)
					.setTitle("选择形状")
					.setSingleChoiceItems(
							new String[] { "路径", "直线", "矩形", "圆形", "实心矩形",
									"实心圆" }, 0,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which + 1) {
									case 1:
										mDoodle.setType(Doodle.ActionType.Path);
										break;
									case 2:
										mDoodle.setType(Doodle.ActionType.Line);
										break;
									case 3:
										mDoodle.setType(Doodle.ActionType.Rect);
										break;
									case 4:
										mDoodle.setType(Doodle.ActionType.Circle);
										break;
									case 5:
										mDoodle.setType(Doodle.ActionType.FillecRect);
										break;
									case 6:
										mDoodle.setType(Doodle.ActionType.FilledCircle);
										break;
									default:
										break;
									}
									dialog.dismiss();
								}
							}).create();
		}
		mShapeDialog.show();
	}

	/**
	 * 选择画笔粗细
	 */
	private void showSizeDialog() {
		if (mPaintDialog == null) {
			mPaintDialog = new AlertDialog.Builder(this)
					.setTitle("选择画笔粗细")
					.setSingleChoiceItems(new String[] { "细", "中", "粗" }, 0,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										mDoodle.setSize(dip2px(5));
										break;
									case 1:
										mDoodle.setSize(dip2px(10));
										break;
									case 2:
										mDoodle.setSize(dip2px(15));
										break;
									default:
										break;
									}

									dialog.dismiss();

								}
							}).create();
		}
		mPaintDialog.show();
	}

	/**
	 * 显示画笔颜色选择
	 */
	private void showColorDialog() {
		if (mColorDialog == null) {
			mColorDialog = new AlertDialog.Builder(this)
					.setTitle("选择颜色")
					.setSingleChoiceItems(new String[] { "红色", "绿色", "蓝色","黑色" }, 3,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										mDoodle.setColor("#ff0000");
										break;
									case 1:
										mDoodle.setColor("#00ff00");
										break;
									case 2:
										mDoodle.setColor("#0000ff");
										break;
									case 3:
										mDoodle.setColor("#000000");
										break;
									default:
										break;
									}

									ToastUtils.showToast(DoodleActivity.this, "切换为画笔~~", Toast.LENGTH_SHORT);

									mDoodle.setSize(dip2px(5));
									dialog.dismiss();
								}
							}).create();
		}
		mColorDialog.show();
	}

	private int dip2px(float dpValue) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, 1, 1, "保存");
//		return super.onCreateOptionsMenu(menu);
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		if (item.getItemId() == 1) {
//		}
//		return true;
//	}
	
	public static void savePicByPNG(Bitmap b, String filePath) {
		FileOutputStream fos = null;
		try {
//			if (!new File(filePath).exists()) {
//				new File(filePath).createNewFile();
//			}
			fos = new FileOutputStream(filePath);
			if (null != fos) {
				b.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}