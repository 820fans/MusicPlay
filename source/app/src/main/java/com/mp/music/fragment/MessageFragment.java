package com.mp.music.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mp.music.R;
import com.mp.music.activity.ChallengeActivity;
import com.mp.music.activity.ChallengedActivity;
import com.mp.music.activity.MusicTagsListActivity;
import com.mp.music.adapter.ChallengeListAdapter;
import com.mp.music.adapter.ChallengedListAdapter;
import com.mp.music.base.BaseFragment;
import com.mp.music.constants.ChallengeItem;
import com.mp.music.constants.ChallengedItem;
import com.mp.music.db.DatabaseManager;
import com.mp.music.entity.Constants;
import com.mp.music.entity.NetResultData;
import com.mp.music.helper.NetHelper;
import com.mp.music.utils.TitleBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MessageFragment extends BaseFragment {

	//两个下拉刷新
	private SwipeRefreshLayout mSwipedLayout;
	private SwipeRefreshLayout mSwipeLayout;
	//两个recycleview
	private RecyclerView mRecyclerView;
	private RecyclerView mRecycleredView;

	//两个列表对应的Adapter
	private ChallengeListAdapter mAdapter;
	private ChallengedListAdapter mChallengedAdapter;
	//两个列表信息
	private ArrayList<ChallengeItem> mDatas=new ArrayList<>();
	private ArrayList<ChallengedItem> mDataed=new ArrayList<>();

	//TabLayout所需
	private TabLayout mTabLayout;
	private ViewPager mViewPager;
	private LayoutInflater mInflater;
	private List<String> mTitleList = new ArrayList<>();//页卡标题集合
	private View view1, view2;//页卡视图
	private List<View> mViewList = new ArrayList<>();//页卡视图集合

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
		View view = View.inflate(getActivity(), R.layout.frag_message, null);

		new TitleBuilder(view)
				.setTitleText("挑战")
				.setRightText("发起挑战")
				.setRightOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent=new Intent();
						intent.setClass(getActivity(), MusicTagsListActivity.class);
						startActivity(intent);
					}
				})
				.build();

		initTabs(view);

		return view;
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
		view1 = mInflater.inflate(R.layout.item_challengelist, null);
		view2 = mInflater.inflate(R.layout.item_challengedlist, null);

		//添加页卡视图
		mViewList.add(view1);
		mViewList.add(view2);

		//添加页卡标题
		mTitleList.add("挑战列表");
		mTitleList.add("挑战过的");

		mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
		mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
		mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));

		MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList);
		mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
		mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
		mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器


		//绑定Adapter
		bindAdapter(view1);
		bindChallengedAdapter(view2);
//		bindOnlieAdapter(view2);
	}

	public void bindAdapter(View view){

		//下拉监听
		mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.id_swipe_ly);

		mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						mSwipeLayout.setRefreshing(false);

						GetChallengeData();
					}
				}, 2000);

			}
		});
		//得到控件
		mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_challengelist);
		mRecyclerView.setHasFixedSize(true);
		//设置布局管理器
		//设置布局管理器
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(linearLayoutManager);
		//设置适配器
		mAdapter = new ChallengeListAdapter(getActivity(), mDatas);

		mAdapter.setOnItemClickLitener(new ChallengeListAdapter.OnItemClickLitener() {
			@Override
			public void onItemClick(View view, int position) {

				Intent intent = new Intent();
				intent.putExtra("id", mDatas.get(position).getId());
				intent.putExtra("title", mDatas.get(position).getChallenge_name());
				intent.putExtra("avatar", mDatas.get(position).getAvatar());
				intent.setClass(getActivity(), ChallengeActivity.class);
				startActivity(intent);

//				Toast.makeText(getActivity(), position + "", Toast.LENGTH_SHORT)
//						.show();
			}
		});

		mRecyclerView.setAdapter(mAdapter);

		GetChallengeData();
	}

	public void bindChallengedAdapter(View view){

		//下拉监听
		mSwipedLayout = (SwipeRefreshLayout) view.findViewById(R.id.id_swipe_ly);

		mSwipedLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						mSwipedLayout.setRefreshing(false);

						GetChallengedData();
					}
				}, 2000);
			}
		});
		//得到控件
		mRecycleredView = (RecyclerView) view.findViewById(R.id.recycle_challengedlist);
		mRecycleredView.setHasFixedSize(true);
		//设置布局管理器
		//设置布局管理器
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecycleredView.setLayoutManager(linearLayoutManager);
		//设置适配器
		mChallengedAdapter = new ChallengedListAdapter(getActivity(), mDataed);

		mChallengedAdapter.setOnItemClickLitener(new ChallengedListAdapter.OnItemClickLitener() {
			@Override
			public void onItemClick(View view, int position) {

				Intent intent=new Intent();
//				System.out.println("Click:"+mDataed.get(position).getId());
				intent.putExtra("id",mDataed.get(position).getId());
				intent.putExtra("title",mDataed.get(position).getChallenge_name());
				intent.putExtra("avatar",mDataed.get(position).getAvatar());
				intent.setClass(getActivity(), ChallengedActivity.class);
				startActivity(intent);

//				Toast.makeText(getActivity(), position + "", Toast.LENGTH_SHORT)
//						.show();
			}
		});

		mRecycleredView.setAdapter(mChallengedAdapter);

		GetChallengedData();
	}

	@Override
	public void onResume() {
		super.onResume();

		GetChallengeData();
		GetChallengedData();
	}

	public void GetChallengeData(){

		//获取挑战
		NetHelper netHelper = new NetHelper(Constants.GET_CHALLENGE);
		netHelper.setParams("user_id", DatabaseManager.getInstance(getActivity()).getActiveUser()+"");
		netHelper.setResultListener(new NetHelper.ResultListener() {

			@Override
			public void getResult(NetResultData result) {

				JSONArray musicList = result.getData();

				mDatas.clear();
				try {

					//处理JSON数组数据
					for (int i = 0; i < musicList.length(); i++) {
						JSONObject item = (JSONObject) musicList.opt(i);

						ChallengeItem challengeItem = new ChallengeItem(
								item.getInt("id"),
								item.getString("avatar"),
								item.getString("challenge_name"),
								item.getString("upper_name"),
								item.getString("upper_time"));

						if(!mDatas.contains(challengeItem))
							mDatas.add(challengeItem);

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

	public void GetChallengedData(){

		//获取挑战
		NetHelper netHelper = new NetHelper(Constants.GET_CHALLENGED);
		netHelper.setParams("user_id", DatabaseManager.getInstance(getActivity()).getActiveUser()+"");
		netHelper.setResultListener(new NetHelper.ResultListener() {

			@Override
			public void getResult(NetResultData result) {

				JSONArray musicList = result.getData();

				mDataed.clear();
				try {

					//处理JSON数组数据
					for (int i = 0; i < musicList.length(); i++) {
						JSONObject item = (JSONObject) musicList.opt(i);

						ChallengedItem challengedItem = new ChallengedItem(
								item.getInt("id"),
								item.getString("avatar"),
								item.getString("challenge_name"),
								item.getString("upper_name"),
								item.getString("upper_time"));

						if(!mDataed.contains(challengedItem))
							mDataed.add(challengedItem);

					}

					mChallengedAdapter.notifyDataSetChanged();
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

}
