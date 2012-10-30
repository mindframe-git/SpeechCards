package com.mindframe.speechcards.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mindframe.speechcards.R;
import com.mindframe.speechcards.model.Category;

public class CategoryAdapter extends ArrayAdapter {

	Context context;
	List<Category> catList;
	
	public CategoryAdapter(Context _context, int resourceId, List<Category> _catList){
		super(_context, resourceId, _catList);
		this.catList = _catList;
		this.context = _context;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.category_line, null);
		
		TextView tvCat = (TextView)convertView.findViewById(R.id.tvCat);
		tvCat.setTypeface(Typeface.createFromAsset(context.getAssets(), "FONT.TTF"));
		
		Category cat = catList.get(position);
		
		tvCat.setText(cat.getName());
		tvCat.setBackgroundColor(cat.getColorCode());
		
		return convertView;
	}
	
	
	
	
	
}
