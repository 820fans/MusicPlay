package com.mp.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mp.music.R;
import com.mp.music.constants.MusicItem;
import com.mp.music.constants.ProcessItem;

import java.util.ArrayList;

public class MusicListAdapter extends  RecyclerView.Adapter<MusicListAdapter.ViewHolder>
{

    /**
     * ItemClick的回调接口
     * @author zhy
     *
     */
    public interface OnItemClickLitener{
        void onItemClick(View view, int position);
    }
    private OnItemClickLitener mOnItemClickLitener;
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    /**
     * 下载的回调接口
     * @author zhy
     *
     */
    public interface OnMusicDownClickLitener{
        void onItemClick(View view, int position);
    }
    private OnMusicDownClickLitener mOnMusicDownClickLitener;
    public void setOnMusicDownClickLitener(OnMusicDownClickLitener mOnMusicDownClickLitener) {
        this.mOnMusicDownClickLitener = mOnMusicDownClickLitener;
    }


    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<MusicItem> mDatas;
    public ArrayList<ProcessItem> processes;

    public MusicListAdapter(Context context, ArrayList<MusicItem> datats,ArrayList<ProcessItem> processes)
    {
        mInflater = LayoutInflater.from(context);
        this.context=context;
        mDatas = datats;
        this.processes=processes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View view)
        {
            super(view);

            mName=(TextView) view.findViewById(R.id.music_name);
            mType=(TextView) view.findViewById(R.id.music_album);
            mSinger=(TextView) view.findViewById(R.id.music_singer);
            mDown=(ImageView) view.findViewById(R.id.music_download);
            mAdd=(ImageView)view.findViewById(R.id.music_add);
            processBar=(ProgressBar) view.findViewById(R.id.progressBar);
        }

        TextView mName;
        TextView mType;
        TextView mSinger;
        ImageView mDown;
        ImageView mAdd;
        ProgressBar processBar;//播放进度
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
        View view = mInflater.inflate(R.layout.item_music,
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
        //NetworkImageView networkImageView = (NetworkImageView) findViewById(R.id.network_image_view);
//        viewHolder.mImg.setDefaultImageResId(R.drawable.ic_launcher);
//        viewHolder.mImg.setErrorImageResId(R.drawable.ic_launcher);
//        viewHolder.mImg.setImageUrl(Constants.SERVER_URL + mDatas.get(i).getCover(), ImageHelper.getLoader());
        viewHolder.mName.setText(mDatas.get(i).getName());
        viewHolder.mSinger.setText(mDatas.get(i).getSinger());
        viewHolder.mType.setText(mDatas.get(i).getType());

        viewHolder.processBar.setMax(processes.get(i).getMax());
        viewHolder.processBar.setProgress(processes.get(i).getProcess());


        if(mOnMusicDownClickLitener!=null){
            viewHolder.mDown.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnMusicDownClickLitener.onItemClick(viewHolder.itemView,i);
                }
            });
        }



        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            viewHolder.mAdd.setOnClickListener(new OnClickListener()
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
