package com.example.ohsu.material_parallaxdesign.parallax.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ohsu.material_parallaxdesign.R;
import com.example.ohsu.material_parallaxdesign.parallax.data.ViewData;

import java.util.ArrayList;

public class RecyleViewAdapter extends RecyclerView.Adapter {
	private Context mContext = null;
	private LayoutInflater mInflater = null;
	private ArrayList<ViewData> item = null;
	public RecyleViewAdapter(Context context, ArrayList<ViewData> list) {
		this.mContext = context;
		this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.item = list;
	}
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return item.get(position).getType();
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return item.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
		// TODO Auto-generated method stub
		View convertView;
		if(viewtype == 0) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_review_detail_top, parent, false);
		} else {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_review_content, parent, false);
		}

		ViewHolder vh = new ViewHolder(convertView);
		
        return vh;
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleText;
        public ViewHolder(View view) {
            super(view);
            //mTitleText = (TextView) view.findViewById(R.id.title_type1);
        }
    }

	@Override
	public void onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	
}
