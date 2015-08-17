package com.example.ohsu.material_parallaxdesign.parallax.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.ohsu.material_parallaxdesign.R;
import com.example.ohsu.material_parallaxdesign.parallax.data.ViewData;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter{
	private Context mContext = null;
	private LayoutInflater mInflater = null;
	private ArrayList<ViewData> item = null;
	public ListViewAdapter(Context context, ArrayList<ViewData> list) {
		this.mContext = context;
		this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.item = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return item.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return item.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return item.get(position).getType();
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewData data = item.get(position);
		if(data !=null) {
			if(data.getType() == 0) {
				convertView = mInflater.inflate(R.layout.cell_review_detail_top, parent, false);
			} else {
				convertView = mInflater.inflate(R.layout.cell_review_content, parent, false);
			}
		}
		return convertView;
	}
			
}
