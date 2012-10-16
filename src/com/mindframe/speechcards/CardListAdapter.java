package com.mindframe.speechcards;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public class CardListAdapter extends ArrayAdapter{
	
	Context context;
	List<Card> cardList;
	BaseDatosHelper bdh;
	
	final String TAG = getClass().getName();

	
	@SuppressWarnings("unchecked")
	CardListAdapter(Context _context, int resourceID, List<Card> _cardList){
		super(_context, resourceID, _cardList);
		this.context = _context;
		this.cardList = _cardList;
	}
	@SuppressWarnings("static-access")
	public View getView(final int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.cardline, null);
		
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 2);
		
		final TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
		final ImageView btnEdit = (ImageView)convertView.findViewById(R.id.btnEdit);
		final ImageView btnRemove = (ImageView)convertView.findViewById(R.id.btnRemove);
		tvTitle.setText(cardList.get(position).header);
		

		
		btnEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, EditCardActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Bundle bun = new Bundle();
				bun.putInt("id_card", cardList.get(position).getId_card());
				
				bun.putString("speechTitle", bdh.getSpeechById(cardList.get(position).id_speech).title);
				intent.putExtras(bun);
				context.startActivity(intent);
			}
		});
		
		
		btnRemove.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				//TODO: hacer botón de confirmación para borrar tarjetas.
				
				bdh.delCard(cardList.get(position));
				remove(cardList.get(position));
				notifyDataSetChanged();
			}
		});
		
		
		
		return convertView;
		
		
	}
	
}
