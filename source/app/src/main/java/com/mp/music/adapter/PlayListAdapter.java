package com.mp.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mp.music.R;
import com.mp.music.constants.PlayItem;

import java.util.ArrayList;

/**
 * Created by 张伟 on 2016-04-22.
 */
public class PlayListAdapter extends  RecyclerView.Adapter<PlayListAdapter.ViewHolder>
{

    /**
     * ItemClick的回调接口
     * @author zhy
     *
     */
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<PlayItem> mDatas;

    public PlayListAdapter(Context context, ArrayList<PlayItem> datats)
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

            mName=(TextView) view.findViewById(R.id.music_name);
            mSinger=(TextView) view.findViewById(R.id.music_singer);
            music_delete=(ImageView)view.findViewById(R.id.music_delete);
        }

        TextView mName;
        TextView mSinger;
        ImageView music_delete;
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
        View view = mInflater.inflate(R.layout.item_playlist,
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

        viewHolder.mName.setText(mDatas.get(i).getName());
        viewHolder.mSinger.setText(mDatas.get(i).getSinger());

        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            viewHolder.music_delete.setOnClickListener(new View.OnClickListener()
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
