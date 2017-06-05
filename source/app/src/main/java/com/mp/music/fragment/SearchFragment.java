package com.mp.music.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mp.music.R;
import com.mp.music.activity.TalkListActivity;
import com.mp.music.adapter.SectionAdapter;
import com.mp.music.base.BaseFragment;
import com.mp.music.entity.Section;
import com.mp.music.utils.TitleBuilder;

import java.util.ArrayList;


public class SearchFragment extends BaseFragment {
	
	private View view;
	private RecyclerView mRecyclerView;
	private SectionAdapter mAdapter;
	private ArrayList<Section> mDatas=new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.frag_search, null);

		initView();

		mockData();

		return view;
	}

	private void initView(){

		new TitleBuilder(view)
				.setTitleText("社区")
				.build();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//得到控件
		mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycle_sectionlist);
		mRecyclerView.setHasFixedSize(true);
		//设置布局管理器
		//设置布局管理器
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(linearLayoutManager);
		//设置适配器
		mAdapter = new SectionAdapter(getActivity(),mDatas );

		mAdapter.setOnItemClickLitener(new SectionAdapter.OnItemClickLitener() {
			@Override
			public void onItemClick(View view, int position) {


				Intent intent=new Intent();
				intent.setClass(getActivity(), TalkListActivity.class);
				intent.putExtra("section_id", mDatas.get(position).getId());
				intent.putExtra("res",mDatas.get(position).getImg());
				intent.putExtra("section",mDatas.get(position).getSection());
				startActivity(intent);
			}
		});

		mRecyclerView.setAdapter(mAdapter);

	}

	private void mockData() {

		Section msg1 = new Section(1,R.drawable.messagescenter_at,"求助");
		mDatas.add(msg1);

		Section msg2 = new Section(2,R.drawable.messagescenter_comments,"讨论");
		mDatas.add(msg2);

		Section msg3 = new Section(3,R.drawable.messagescenter_good,"精华");
		mDatas.add(msg3);

		Section msg4 = new Section(4,R.drawable.messagescenter_messagebox,"灌水");
		mDatas.add(msg4);

	}
}
