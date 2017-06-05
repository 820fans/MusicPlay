package com.mp.music.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mp.music.R;
import com.mp.music.constants.GuessDetailItem;

import java.util.ArrayList;

/**
 * Created by 张伟 on 2016-04-06.
 */
public class GuessTimeDetailAdapter extends  RecyclerView.Adapter<GuessTimeDetailAdapter.ViewHolder>{

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

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<GuessDetailItem> mDatas;

    public GuessTimeDetailAdapter(Context context, ArrayList<GuessDetailItem> datats)
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

            mGuessContent=(TextView) view.findViewById(R.id.guess_content);
            mGuessTrue=(TextView) view.findViewById(R.id.guess_true);
            mGuessTime=(TextView) view.findViewById(R.id.guess_time);
        }
        TextView mGuessContent;
        TextView mGuessTime;
        TextView mGuessTrue;
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
        View view = mInflater.inflate(R.layout.item_guessdetail,
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
//        viewHolder.mGuessTime.setText(mDatas.get(i));
        viewHolder.mGuessContent.setText(mDatas.get(i).getAnswer());
        viewHolder.mGuessTime.setText(mDatas.get(i).getTime()+"");

        if(mDatas.get(i).getDone().equals("是")) {

            Resources resource = (Resources) context.getResources();
            ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.green_geek);
            if (csl != null) {

                viewHolder.mGuessTrue.setTextColor(csl);
                viewHolder.mGuessTrue.setText(mDatas.get(i).getDone()+"");
            }
        }
        else{
            Resources resource = (Resources) context.getResources();
            ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.common_red);
            if (csl != null) {

                viewHolder.mGuessTrue.setTextColor(csl);
                viewHolder.mGuessTrue.setText(mDatas.get(i).getDone() + "");
            }
        }

        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });

        }
    }
}
