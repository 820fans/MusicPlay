package com.mp.music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mp.music.R;
import com.mp.music.adapter.TagListAdapter;
import com.mp.music.base.BaseActivity;
import com.mp.music.constants.TagItem;
import com.mp.music.entity.Constants;
import com.mp.music.entity.NetResultData;
import com.mp.music.helper.NetHelper;
import com.mp.music.utils.TitleBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 张伟 on 2016-04-07.
 */
public class MusicTagsListActivity extends BaseActivity{

    private RecyclerView mRecyclerView;
    private TagListAdapter mAdapter;
    private ArrayList<TagItem> mDatas=new ArrayList<TagItem>();
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);

        new TitleBuilder(this)
                .setTitleText("请选择标签")
                .setLeftImage(R.drawable.back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        MusicTagsListActivity.this.finish();
                    }
                });

        //得到控件
        mRecyclerView = (RecyclerView)this.findViewById(R.id.recycle_taglist);
        mRecyclerView.setHasFixedSize(true);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //设置适配器
        mAdapter = new TagListAdapter(this, mDatas);

        mRecyclerView.setAdapter(mAdapter);

        //监听按键
        mAdapter.setOnItemClickLitener(new TagListAdapter.OnItemClickLitener() {

            @Override
            public void onItemClick(View view, int position) {

                Intent intent=new Intent();
                intent.setClass(MusicTagsListActivity.this, DoodleActivity.class);
                intent.putExtra("id", mDatas.get(position).getId());
                intent.putExtra("music", mDatas.get(position).getMusic());
                intent.putExtra("tag", mDatas.get(position).getTag());
                startActivity(intent);
                MusicTagsListActivity.this.finish();
            }
        });

        int music_id=getIntent().getIntExtra("music_id",-1);

        //获取音乐标签
        NetHelper netHelper = new NetHelper(Constants.GET_TAGS);
        netHelper.setParams("music_id",music_id+"");
        netHelper.setResultListener(new NetHelper.ResultListener() {

            @Override
            public void getResult(NetResultData result) {

                JSONArray musicList = result.getData();

                try {

                    //处理JSON数组数据
                    for (int i = 0; i < musicList.length(); i++) {
                        JSONObject item = (JSONObject) musicList.opt(i);

                        TagItem tag = new TagItem(item.getInt("id"),
                                item.getString("music"),
                                item.getString("tag"));
                        mDatas.add(tag);

                    }

                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getError() {
                Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });

        netHelper.doPost();
    }

}
