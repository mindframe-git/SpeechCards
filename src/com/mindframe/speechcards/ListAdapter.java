package com.mindframe.speechcards;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter {

	Context context;
	List<Speech> speechList;
	BaseDatosHelper bdh;
	
	ListAdapter(Context _context, int resourceId, List<Speech> _speechList) {
		super(_context, resourceId, _speechList);
		this.speechList = _speechList;
		this.context = _context;
		
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.linelist, null);
		
		TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
		TextView tvNCard = (TextView)convertView.findViewById(R.id.tvNCard);
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 2);
		
		Speech speech = speechList.get(position);
		
		Typeface font = Typeface.createFromAsset(context.getAssets(), "FONT.TTF");
		
		tvTitle.setTypeface(font);
		tvNCard.setTypeface(font);
		
		tvTitle.setText(speech.title);
		List<Card> cardList = bdh.getCardsByIdSpeech(speech.id_speech);
		if(cardList.isEmpty()){
			tvNCard.setText("No hay tarjetas.");
		}else if(cardList.size() == 1){
			tvNCard.setText("1 tarjeta.");
		}else{
			tvNCard.setText(cardList.size() + " tarjetas.");
		}
		
		return convertView;
	}
	
}
