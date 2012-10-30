package com.mindframe.speechcards.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindframe.speechcards.BaseDatosHelper;
import com.mindframe.speechcards.EditCardActivity;
import com.mindframe.speechcards.R;
import com.mindframe.speechcards.model.Card;

@SuppressWarnings("rawtypes")
public class CardListAdapter extends ArrayAdapter{
	
	Context context;
	List<Card> cardList;
	BaseDatosHelper bdh;
	
	final String TAG = getClass().getName();

	
	@SuppressWarnings("unchecked")
	public CardListAdapter(Context _context, int resourceID, List<Card> _cardList){
		super(_context, resourceID, _cardList);
		this.context = _context;
		this.cardList = _cardList;
	}
	@SuppressWarnings("static-access")
	public View getView(final int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.manage_speech_line, null);
		
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 3);
		
		final TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
		final ImageView btnEdit = (ImageView)convertView.findViewById(R.id.btnEdit);
		tvTitle.setText(cardList.get(position).getHeader());
		
		Typeface font = Typeface.createFromAsset(context.getAssets(), "FONT.TTF");
		tvTitle.setTypeface(font);
		
		btnEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, EditCardActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Bundle bun = new Bundle();
				bun.putInt("id_card", cardList.get(position).getId_card());
				
				bun.putString("speechTitle", bdh.getSpeechById(cardList.get(position).getId_speech()).getTitle());
				intent.putExtras(bun);
				context.startActivity(intent);
			}
		});
		
		return convertView;
		
		
	}
	
}
