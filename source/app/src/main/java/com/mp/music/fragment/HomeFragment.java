package com.mp.music.fragment;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mp.music.R;
import com.mp.music.activity.MusicTagsListActivity;
import com.mp.music.adapter.LocalMusicListAdapter;
import com.mp.music.adapter.MusicListAdapter;
import com.mp.music.adapter.PlayListAdapter;
import com.mp.music.base.BaseFragment;
import com.mp.music.constants.LocalMusicItem;
import com.mp.music.constants.MusicItem;
import com.mp.music.constants.PlayItem;
import com.mp.music.constants.ProcessItem;
import com.mp.music.entity.Constants;
import com.mp.music.entity.NetResultData;
import com.mp.music.entity.PopMenu;
import com.mp.music.helper.NetHelper;
import com.mp.music.service.DownloadService;
import com.mp.music.service.OnLinePlayService;
import com.mp.music.utils.TitleBuilder;
import com.mp.music.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends BaseFragment implements OnClickListener{

	protected static final int SEARCH_MUSIC_SUCCESS = 0;// 搜索成功标记

	//三个列表
	private RecyclerView mRecyclerView;
	private RecyclerView mPlayRecyclerView;
	private RecyclerView mLocalRecyclerView;
	//三个列表对应的Adapter
	private MusicListAdapter mAdapter;
	private PlayListAdapter mPlayAdapter;
	private LocalMusicListAdapter mLocalAdapter;

	public static int currentPlay=-1;

	//三个列表信息
	public static ArrayList<MusicItem> onlinelist=new ArrayList<>();
	public static ArrayList<PlayItem> playlist=new ArrayList<>();
	public static ArrayList<LocalMusicItem> locallist=new ArrayList<>();
	public static ArrayList<ProcessItem> processes=new ArrayList<>();
	//将要添加到列表中的本地列表
	public static ArrayList<LocalMusicItem> addlocallist=new ArrayList<>();

	//由上方的小播放器进入播放详情
	private RelativeLayout relativeLayout;
	//上方
	private TextView frag_status;
	private TextView titleAndAuthor;
	private CheckBox rb_musicplay; //播放按钮

	//广播，与Service交互
	MusicBoxReceiver mReceiver=new MusicBoxReceiver();
	DownloadReceiver downloadReceiver=new DownloadReceiver();

	public boolean downloading=false;

	//TabLayout所需
	private TabLayout mTabLayout;
	private ViewPager mViewPager;
	private LayoutInflater mInflater;
	private List<String> mTitleList = new ArrayList<>();//页卡标题集合
	private View view1, view2, view3;//页卡视图
	private List<View> mViewList = new ArrayList<>();//页卡视图集合

	//弹出窗口
	private PopMenu menuWindow;

	public ArrayList<ProgressBar> progressBars=new ArrayList<>();

	//ViewPager适配器
	class MyPagerAdapter extends PagerAdapter {
		private List<View> mViewList;

		public MyPagerAdapter(List<View> mViewList) {
			this.mViewList = mViewList;
		}

		@Override
		public int getCount() {
			return mViewList.size();//页卡数
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;//官方推荐写法
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mViewList.get(position));//添加页卡
			return mViewList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mViewList.get(position));//删除页卡
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTitleList.get(position);//页卡标题
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(
				R.layout.frag_home,
				container, false);
		new TitleBuilder(view).setTitleText("首页").build();

		//绑定顶部状态栏
		relativeLayout=(RelativeLayout) view.findViewById(R.id.player_info_wrapper);
		relativeLayout.setOnClickListener(this);

		rb_musicplay=(CheckBox)view.findViewById(R.id.rb_musicplay);
		rb_musicplay.setOnClickListener(this);

		frag_status=(TextView)view.findViewById(R.id.frag_status);
		titleAndAuthor=(TextView)view.findViewById(R.id.frag_song);

		initTabs(view);

		//实例化SelectPicPopupWindow
		menuWindow = new PopMenu(getActivity(),
				itemsOnClick,seekBarChangeListener);
		menuWindow.setMusic_cover("");

		return view;
	}

	/**
	 * 向音乐列表添加音乐
	 */
	public void AddLocalMusictoPlayList(){

		for(int i=0;i<addlocallist.size();i++){
			PlayItem item=new PlayItem();
			item.setName(addlocallist.get(i).getName());
			item.setSinger(addlocallist.get(i).getSinger());
			item.setUrl(addlocallist.get(i).getPath());
			if(!playlist.contains(item))
			playlist.add(item);
		}

		mPlayAdapter.notifyDataSetChanged();

	}

	/**
	 * 初始化各种选项卡
	 * @param view
	 */
	public void initTabs(View view){
		//各种选项卡
		mViewPager = (ViewPager) view.findViewById(R.id.vp_view);
		mTabLayout = (TabLayout) view.findViewById(R.id.tabs);

		mInflater = LayoutInflater.from(getActivity());
		view1 = mInflater.inflate(R.layout.play_list, null);
		view2 = mInflater.inflate(R.layout.online_list, null);
		view3 = mInflater.inflate(R.layout.local_list, null);

		//添加页卡视图
		mViewList.add(view1);
		mViewList.add(view2);
		mViewList.add(view3);

		//添加页卡标题
		mTitleList.add("播放列表");
		mTitleList.add("在线音乐");
		mTitleList.add("本地音乐");

		mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
		mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
		mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
		mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));

		MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList);
		mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
		mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
		mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器


		//绑定Adapter
		bindPlayAdapter(view1);
		bindOnlieAdapter(view2);
		bindLocalAdapter(view3);
	}

	//异步搜索文件结束，由Handler处理
	private Handler hander = new Handler() {
		public void handleMessage(Message msg) {
//			switch (msg.what) {
//				case SEARCH_MUSIC_SUCCESS:
//					//搜索音乐文件结束时
//					mLocalAdapter.notifyDataSetChanged();
//					break;
//			}
		};
	};

	/**
	 * 绑定本地音乐列表
	 * @param view
	 */
	public void bindLocalAdapter(View view){

		//得到控件
		mLocalRecyclerView = (RecyclerView)view.findViewById(R.id.local_musiclist);
		mLocalRecyclerView.setHasFixedSize(true);
		//设置布局管理器
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mLocalRecyclerView.setLayoutManager(linearLayoutManager);
		//设置适配器
		mLocalAdapter = new LocalMusicListAdapter(getActivity(), locallist);
		mLocalRecyclerView.setAdapter(mLocalAdapter);
		final RelativeLayout addtolistwrapper=(RelativeLayout)view.findViewById(R.id.addtolistwrapper);
		Button submitlist=(Button)view.findViewById(R.id.addtolist);

		submitlist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AddLocalMusictoPlayList();
				ToastUtils.showToast(getActivity(),"已经添加到播放列表",Toast.LENGTH_SHORT);
			}
		});

		mLocalAdapter.setOnItemClickLitener(new LocalMusicListAdapter.OnItemClickLitener() {
			@Override
			public void onItemClick(View view, int position) {

				if (addlocallist.contains(locallist.get(position))) {
					addlocallist.remove(locallist.get(position));
					((CheckBox) view).setChecked(false);
				} else {
					//加入临时列表
					addlocallist.add(locallist.get(position));
					((CheckBox) view).setChecked(true);
				}

				if (addlocallist.size() > 0) {
					addtolistwrapper.setVisibility(View.VISIBLE);
				} else {
					addtolistwrapper.setVisibility(View.GONE);
				}
			}
		});

		locallist.clear();
		ContentResolver mResolver = getActivity().getContentResolver();
		Cursor cursor = mResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		int totalCount=cursor.getCount();
		cursor.moveToFirst();
		for (int i =0;i<totalCount;i++){
			int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
			String path=cursor.getString(cursor.getColumnIndexOrThrow(
					MediaStore.Audio.Media.DATA));
			String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
			String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
			String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
			cursor.moveToNext();
			Log.d("MP3Debug", "MediaStore.Audio.Media._ID:" + id);
			Log.d("MP3Debug","MediaStore.Audio.Media.PATH:"+path);
			Log.d("MP3Debug","MediaStore.Audio.Media.TITLE:"+title);
			Log.d("MP3Debug","MediaStore.Audio.Media.ALBUM:"+album);
			Log.d("MP3Debug","MediaStore.Audio.Media.ARTIST:"+artist);
			LocalMusicItem localMusicItem=new LocalMusicItem(path,title);
			localMusicItem.setSinger(artist);
			localMusicItem.setId(id);
			localMusicItem.setAlbum(album);

			if(!locallist.contains(localMusicItem)){
				locallist.add(localMusicItem);
			}
		}

//		locallist.clear();
//		//是否有外部存储设备
//		if (Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED)) {
//
//			final String[] ext = { ".mp3" };
//			final File file = new File("/mnt/sdcard/");
//
////			System.out.println(file.getAbsolutePath());
//			new Thread(new Runnable() {
//
//				public void run() {
//					MusicFile.search(locallist, file, ext, mLocalAdapter);
//					hander.sendEmptyMessage(SEARCH_MUSIC_SUCCESS);
//				}
//			}).start();
//
//		} else {
//			ToastUtils.showToast(getActivity(), "请插入外部存储设备..", Toast.LENGTH_LONG);
//		}
	}

	/**
	 * 绑定播放列表
	 * @param view
	 */
	public void bindPlayAdapter(View view){

//		PlayItem pi=new PlayItem(1,"东风破","周杰伦");
//		playlist.add(pi);

		//得到控件
		mPlayRecyclerView = (RecyclerView)view.findViewById(R.id.play_musiclist);
		mPlayRecyclerView.setHasFixedSize(true);
		//设置布局管理器
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mPlayRecyclerView.setLayoutManager(linearLayoutManager);
		//设置适配器
		mPlayAdapter = new PlayListAdapter(getActivity(), playlist);
		mPlayRecyclerView.setAdapter(mPlayAdapter);

		mPlayAdapter.setOnItemClickLitener(new PlayListAdapter.OnItemClickLitener() {
			@Override
			public void onItemClick(View view, int position) {

				sendBroadcastToService(Constants.STATE_NEXT);
				playlist.remove(position);

				mPlayAdapter.notifyDataSetChanged();
			}
		});
	}

	/**
	 * 绑定线上音乐列表
	 * @param view
	 */
	public void bindOnlieAdapter(View view){

		//得到控件
		mRecyclerView = (RecyclerView)view.findViewById(R.id.online_musiclist);
		mRecyclerView.setHasFixedSize(true);
		//设置布局管理器
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(linearLayoutManager);
		//设置适配器
		mAdapter = new MusicListAdapter(getActivity(), onlinelist,processes);
		mRecyclerView.setAdapter(mAdapter);

		mAdapter.setOnItemClickLitener(new MusicListAdapter.OnItemClickLitener() {
			@Override
			public void onItemClick(View view, int position) {

				PlayItem item = new PlayItem();
				item.setId(onlinelist.get(position).getId());
				item.setName(onlinelist.get(position).getName());
				item.setSinger(onlinelist.get(position).getSinger());
				item.setCover(Constants.SERVER_URL + onlinelist.get(position).getCover());
				item.setUrl(Constants.SERVER_URL + onlinelist.get(position).getPath());

				if (!playlist.contains(item)) {

					ToastUtils.showToast(getActivity(), onlinelist.get(position).getName() +
							"已经添加到播放列表", Toast.LENGTH_SHORT);
					playlist.add(item);
					mPlayAdapter.notifyDataSetChanged();
				} else {

					ToastUtils.showToast(getActivity(), onlinelist.get(position).getName() +
							" 已经添加过", Toast.LENGTH_SHORT);
				}
			}
		});

		mAdapter.setOnMusicDownClickLitener(new MusicListAdapter.OnMusicDownClickLitener() {
			@Override
			public void onItemClick(View view, int position) {

					sendDownloadBroadcastToService(position,Constants.DOWN_START);

					ToastUtils.showToast(getActivity(), onlinelist.get(position).getName() +
							" 开始下载 ", Toast.LENGTH_SHORT);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//取消注册
		getActivity().unregisterReceiver(downloadReceiver);
		getActivity().unregisterReceiver(mReceiver);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rb_musicplay:

				//播放
				if(rb_musicplay.isChecked()){
					if(HomeFragment.playlist.size()>0) {
						sendBroadcastToService(Constants.STATE_PLAY);
						menuWindow.setBtnPlayPause(true);
					}
					else{
						rb_musicplay.setChecked(false);
						ToastUtils.showToast(getActivity(),"暂无音乐",Toast.LENGTH_SHORT);
					}
				}
				//暂停
				else{
					sendBroadcastToService(Constants.STATE_PAUSE);
					menuWindow.setBtnPlayPause(false);
				}

				break;
			case R.id.player_info_wrapper:

				if(OnLinePlayService.state==Constants.STATE_PLAY ||
						OnLinePlayService.state==Constants.STATE_PAUSE)
				//显示窗口
				menuWindow.showAtLocation(getActivity().findViewById(R.id.main),
						Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
			default:
				break;
		}
	}

	//为弹出窗口实现监听类
    public OnClickListener itemsOnClick =
			new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.cb_musicplay:

					if(menuWindow.getBtnPlayPause()){
						if(HomeFragment.playlist.size()>0) {
							sendBroadcastToService(Constants.STATE_PLAY);
							rb_musicplay.setChecked(true);
						}
					}
					else{
						sendBroadcastToService(Constants.STATE_PAUSE);
						rb_musicplay.setChecked(false);
					}

					break;
				case R.id.ic_previous:
					sendBroadcastToService(Constants.STATE_PREVIOUS);
					break;
				case R.id.ic_next:
					sendBroadcastToService(Constants.STATE_NEXT);
					break;
				case R.id.pop_back:
					menuWindow.dismiss();
					break;
				case R.id.act_challenge:

					Intent intent=new Intent();
					intent.setClass(getActivity(), MusicTagsListActivity.class);
					System.out.println(currentPlay);

					//playlist里面对应着音乐的id，由此寻找tag
					if(currentPlay >=0 &&
							playlist.get(currentPlay)!=null &&
							playlist.get(currentPlay).getId()>0){
						intent.putExtra("music_id",playlist.get(currentPlay).getId());
						startActivity(intent);
						menuWindow.dismiss();
					}
					else{
						ToastUtils.showToast(getActivity(),"没有正在播放的音乐或当前音乐没有标签",Toast.LENGTH_SHORT);
					}

					break;
				default:
					break;
			}
		}
	};

	//弹出窗口的进度条事件监听
	public SeekBar.OnSeekBarChangeListener seekBarChangeListener=new SeekBar.OnSeekBarChangeListener() {

		int progress;
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

			this.progress = progress * OnLinePlayService.getPlayerDuration()
					/ seekBar.getMax();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

			OnLinePlayService.isChanging=true;
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
			sendProcessBroadcastToService(progress);
		}
	};

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//启动后台Service
		Intent intent=new Intent(getActivity(), OnLinePlayService.class);
		getActivity().startService(intent);
		//注册接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.MUSICBOX_ACTION);
		getActivity().registerReceiver(mReceiver, filter);

		//启动下载服务
		Intent intent1=new Intent(getActivity(), DownloadService.class);
		getActivity().startService(intent1);
		//注册接收器
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction(Constants.DOWNBOX_ACTION);
		getActivity().registerReceiver(downloadReceiver, filter1);

		GetNetMusicList();
	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("Resume");
	}

	public void TestCloud(){

		//获取音乐
		NetHelper netHelper = new NetHelper(Constants.SERVER_URL + "getCloudMusic.php");
		netHelper.setParams("name","千年泪");
		netHelper.setResultListener(new NetHelper.ResultListener() {

			@Override
			public void getResult(NetResultData result) {
			}

			@Override
			public void getError() {
				Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
			}
		});

		netHelper.doPost();
	}

	/**
	 * 获取网络音乐列表
	 */
	public void GetNetMusicList(){

//		TestCloud();
		//获取音乐
		NetHelper netHelper = new NetHelper(Constants.GET_MUSIC);
		netHelper.setResultListener(new NetHelper.ResultListener() {

			@Override
			public void getResult(NetResultData result) {

				JSONArray musicList = result.getData();

				try {

					//处理JSON数组数据
					for (int i = 0; i < musicList.length(); i++) {
						JSONObject item = (JSONObject) musicList.opt(i);

						MusicItem music = new MusicItem(item.getInt("id"),
								item.getString("name"),
								item.getString("cover"),
								item.getString("path"),
								item.getString("singer"),
								item.getString("type"));

						if (!onlinelist.contains(music)) {
							onlinelist.add(music);
							ProcessItem processItem=new ProcessItem();
							processes.add(processItem);
						}
					}

					mAdapter.notifyDataSetChanged();
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

	/**
	 *向后台Service发送控制广播，用于控制音乐播放状态
	 *@param state int state 控制状态码
	 * */
	protected void sendBroadcastToService(int state) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setAction(Constants.MUSICSERVICE_ACTION);
		intent.putExtra("control", state);
		//向后台Service发送播放控制的广播
		getActivity().sendBroadcast(intent);
	}

	/**
	 * 向后台发送进度条广播，用于更新在线音乐播放进度
	 * @param process 进度条进度
	 */
	protected void sendProcessBroadcastToService(int process){
		Intent intent=new Intent();
		intent.setAction(Constants.MUSICSERVICE_ACTION);
		intent.putExtra("control", Constants.PLAY_PROCESS);
		intent.putExtra("process",process);
		//向后台Service发送播放控制的广播
		getActivity().sendBroadcast(intent);
	}

	/**
	 * 向后台发送广播，用于实现对后台下载的控制
	 * @param action 下载行为
	 */
	protected void sendDownloadBroadcastToService(int position,int action){
		System.out.println(position+","+action+onlinelist.get(position).getPath());
		Intent intent=new Intent();
		intent.setAction(Constants.DOWNSERVICE_ACTION);
		intent.putExtra("position",position);
		intent.putExtra("action", action);//下载服务请求类型
		//向后台Service发送播放控制的广播
		getActivity().sendBroadcast(intent);
	}

	//创建一个广播接收器用于接收后台Service发出的广播，更新前台页面
	class DownloadReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			int action=intent.getIntExtra("action",-1);
			System.out.println("act:"+action);
			switch (action){
				case Constants.DOWN_SIZE:
					int position=intent.getIntExtra("position",-1);
					int size=intent.getIntExtra("size", -1);

					processes.get(position).setMax(size);
					mAdapter.notifyItemChanged(position);
					break;
				case Constants.DOWN_PROCESS:

					int position1=intent.getIntExtra("position",-1);
					int size1=intent.getIntExtra("size",-1);

					processes.get(position1).setProcess(size1);
					mAdapter.notifyItemChanged(position1);

					if(processes.get(position1).getMax() == processes.get(position1).getProcess()){
						processes.get(position1).setProcess(0);
						ToastUtils.showToast(getActivity(), onlinelist.get(position1).getName() +
								" 下载完成", Toast.LENGTH_LONG);

						//更新本地音乐列表
						LocalMusicItem localMusicItem=new LocalMusicItem(
								Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
										.getAbsolutePath()+"/"
										+onlinelist.get(position1).getName()+".mp3",
								onlinelist.get(position1).getName()
						);
						localMusicItem.setSinger(onlinelist.get(position1).getSinger());
						locallist.add(localMusicItem);
						mLocalAdapter.notifyDataSetChanged();

					}

					break;
			}

		}
	}

	//创建一个广播接收器用于接收后台Service发出的音乐广播
	class MusicBoxReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			// 获取Intent中的current消息，current代表当前正在播放的歌曲
			int current = intent.getIntExtra("current", -1);
			if(current>=0){

				currentPlay=current;

				String title=HomeFragment.playlist.get(current).getName();//更新音乐标题
				String author = HomeFragment.playlist.get(current).getSinger();//更新音乐作者
				frag_status.setText("正在播放");
				titleAndAuthor.setText(title + " - " + author);

				menuWindow.setMusicNameandSinger(title, author);
				menuWindow.setBtnPlayPause(true);
				rb_musicplay.setChecked(true);

			}
			//说明是一个暂停指令
			else if(current == -2){

//				System.out.println("接受到暂停指令");
				menuWindow.setBtnPlayPause(false);
				rb_musicplay.setChecked(false);
			}
			//说明是更新进度条的广播
			else if(current==-3) {

//				System.out.println("接受到进度条指令");
				float percent=intent.getFloatExtra("percent",0);
				int bufferPercent=intent.getIntExtra("bufferPercent", 0);
				int position=intent.getIntExtra("position", 0);

				String title=HomeFragment.playlist.get(position).getName();//更新音乐标题
				String author = HomeFragment.playlist.get(position).getSinger();//更新音乐作者

				if(!frag_status.getText().equals("正在播放"))
					frag_status.setText("正在播放");
				if(!titleAndAuthor.getText().equals(title + " - " + author))
					titleAndAuthor.setText(title + " - " + author);
				rb_musicplay.setChecked(true);

				menuWindow.setSeekBarProgress(percent);
				menuWindow.setSeekBarSecondProgress(bufferPercent);
				//System.out.println(playlist.get(position).getUrl());
				menuWindow.setMusic_cover(playlist.get(position).getCover());
				menuWindow.setBtnPlayPause(true);
				menuWindow.setMusicNameandSinger(title,author);
				//修改进度条
//				seekBar.setProgress(seekBar.getMax() * position / duration);
			}
			//没有音乐的通知
			else if(current==-4){
				ToastUtils.showToast(getActivity(),"播放列表里面暂无音乐",Toast.LENGTH_LONG);
				rb_musicplay.setChecked(false);
				menuWindow.setBtnPlayPause(false);
			}
			//音乐停止的通知
			else if(current==-5){

				frag_status.setText("已停止");
				titleAndAuthor.setText("---");
				menuWindow.setBtnPlayPause(false);
				rb_musicplay.setChecked(false);

			}

		}
	}

}
