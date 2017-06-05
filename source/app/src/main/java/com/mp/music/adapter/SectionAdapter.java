package com.mp.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mp.music.R;
import com.mp.music.entity.Section;

import java.util.ArrayList;


/**
 * Created by 张伟 on 2016-04-06.
 */
public class SectionAdapter extends  RecyclerView.Adapter<SectionAdapter.ViewHolder>{

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
	private ArrayList<Section> mDatas;

	public SectionAdapter(Context context, ArrayList<Section> datats)
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

			iv_section_item = (ImageView) view.findViewById(R.id.iv_section_item);
			tv_section_item = (TextView) view.findViewById(R.id.tv_section_item);
		}

		public ImageView iv_section_item;
		public TextView tv_section_item;
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
		View view = mInflater.inflate(R.layout.item_section,
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

		viewHolder.iv_section_item.setImageResource(mDatas.get(i).getImg());
		viewHolder.tv_section_item.setText(mDatas.get(i).getSection());

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
