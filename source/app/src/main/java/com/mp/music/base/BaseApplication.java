package com.mp.music.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mp.music.R;
import com.mp.music.entity.Constants;
import com.mp.music.entity.User;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.LinkedList;
import java.util.List;

public class BaseApplication extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();

//	public static ImageLoader imageHelper = ImageHelper.getLoader();

	private ImageLoaderConfiguration mImageLoaderConfiguration;
	private static DisplayImageOptions mDisplayOptCommon;

	private  SharedPreferences mPreferences = null;

	private static BaseApplication instance = new BaseApplication();

	public static ImageLoader imageLoader=ImageLoader.getInstance();

	// 单例模式中获取唯一的ExitApplication 实例
	public static BaseApplication getInstance() {
		if (null == instance) {
			instance = new BaseApplication();
		}

		return instance;
	}

	public static RequestQueue netQueue;

	public User currentUser;

	@Override
	public void onCreate() {
		super.onCreate();

		// 不必为每一次HTTP请求都创建一个RequestQueue对象，推荐在application中初始化
		netQueue = Volley.newRequestQueue(this);
		initImageLoader(this);
	}

	// 初始化图片处理
	private void initImageLoader(Context context) {

		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
//如Bitmap.Config.ARGB_8888
				.showImageOnLoading(R.drawable.appicon)   //默认图片
				.showImageForEmptyUri(R.drawable.img_failure)    //url爲空會显示该图片，自己放在drawable里面的
				.showImageOnFail(R.drawable.img_failure)// 加载失败显示的图片
				.displayer(new RoundedBitmapDisplayer(50))  //圆角，不需要请删除
				.build();


		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this)
				.memoryCacheExtraOptions(480, 800)// 缓存在内存的图片的宽和高度
//				.discCacheExtraOptions(480, 800, Bitmap.CompressFormat.PNG,70,null)
				 //CompressFormat.PNG类型，70质量（0-100）
				.memoryCache(new WeakMemoryCache())
				.memoryCacheSize(2 * 1024 * 1024) //缓存到内存的最大数据
				.discCacheSize(50 * 1024 * 1024).  //缓存到文件的最大数据
						discCacheFileCount(1000)            //文件数量
				.defaultDisplayImageOptions(options).  //上面的options对象，一些属性配置
						build();
		ImageLoader.getInstance().init(config); //初始化
	}

	// 添加Activity 到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 从容器中删除Activity
	public void removeActivity(Activity activity) {
		activityList.remove(activity);
	}

	public void removeAllActivity(){
		System.out.println(activityList.size());
		activityList.clear();
	}

	public SharedPreferences getPreferences() {
		if (mPreferences == null) {
			mPreferences = getSharedPreferences(Constants.SP_NAME, MODE_PRIVATE);
		}
		return mPreferences;
	}

	public int getUserId() {
		return getPreferences().getInt(Constants.KEY_USER_ID, -1);
	}

	// 遍历所有Activity 并finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}

	public float getLatitude() {
		return getPreferences().getFloat(Constants.KEY_LATITUDE, 0);
	}

	public void setLatitude(float latitude) {
		Editor editor = getPreferences().edit();
		editor.putFloat(Constants.KEY_LATITUDE, latitude);
		editor.commit();
	}
}
