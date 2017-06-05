package com.mp.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mp.music.R;
import com.mp.music.constants.TagItem;

import java.util.ArrayList;

/**
 * Created by 张伟 on 2016-04-07.
 */
public class TagListAdapter extends  RecyclerView.Adapter<TagListAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<TagItem> mDatas;

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public TagListAdapter(Context context, ArrayList<TagItem> datats)
    {
        mInflater = LayoutInflater.from(context);
        this.context=context;
        mDatas = datats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View view)
        {
            super(view);

            mMusic=(TextView) view.findViewById(R.id.tag_music_name);
            mTag=(TextView) view.findViewById(R.id.tag);
        }

        TextView mMusic;
        TextView mTag;
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.item_tag,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        viewHolder.mMusic.setText(mDatas.get(i).getMusic());
        viewHolder.mTag.setText(mDatas.get(i).getTag());

        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });
        }
    }

}
