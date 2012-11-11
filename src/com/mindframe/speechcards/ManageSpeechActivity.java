package com.mindframe.speechcards;


import java.util.ArrayList;
import java.util.List;

import com.mindframe.speechcards.adapter.CardListAdapter;
import com.mindframe.speechcards.model.Card;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ManageSpeechActivity extends Activity{
	
	Context context;
	BaseDatosHelper bdh;
	final String TAG = getClass().getName();
	
	ImageView btnAddCard;
	EditText etNewCardHeader;
	ListView lvCardList;
	TextView tvTitleSpeech;
	
	int id_speech;
	String speechTitle;
	List<Card> cardList;
	Card lastCard;
	boolean grabado;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		context = this.getApplicationContext();
		setContentView(R.layout.manage_speech);
		
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 3);
		
		btnAddCard = (ImageView)findViewById(R.id.btnAddCard);
		etNewCardHeader = (EditText)findViewById(R.id.etNewCardHeader);
		lvCardList = (ListView)findViewById(R.id.lvCardList);
		tvTitleSpeech = (TextView)findViewById(R.id.tvTitleSpeech);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "FONT.TTF");
		tvTitleSpeech.setTypeface(font);
		etNewCardHeader.setTypeface(font);
		
		Bundle bundle = getIntent().getExtras();
		id_speech = bundle.getInt("id_speech");
		speechTitle = bundle.getString("speechTitle");

		cardList = new ArrayList<Card>();
		
		loadCardList();
		
		//Al pulsar enter, agregar tarjeta
		grabado = false;
		etNewCardHeader.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					if(!grabado){
						addCard();
						grabado = true;
						return true;
					}
				}
				grabado = false;
				return false;
			}
		});
		
		btnAddCard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addCard();
			}
		});
				
		lvCardList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				crearDialogoConfirmacion(arg2);
				return false;
			}
		});
		
	}
	
	private Dialog crearDialogoConfirmacion(final int position) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialTitle);
		builder.setMessage(R.string.dialMessageCard);
		builder.setPositiveButton(R.string.dialAcept, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				bdh.delCard(cardList.get(position));
				Toast.makeText(context, R.string.toastDelCard, Toast.LENGTH_SHORT).show();
				loadCardList();
				dialog.cancel();
			}
		});
		builder.setNegativeButton(R.string.dialCancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		return builder.show();
	}

	protected void addCard() {
		//Controlar cabecera vacía:
		
		if(etNewCardHeader.getText().toString().trim().compareToIgnoreCase("") == 0){
			Toast.makeText(context, R.string.toastVoidHeader, Toast.LENGTH_SHORT).show();
		}else{
				Card card = new Card();
				card.setHeader(etNewCardHeader.getText().toString().trim());
				card.setId_next_card(0);
				card.setId_speech(id_speech);
				if(cardList.size() == 0){
					card.setId_prev_card(0);
					card.setId_card(bdh.addCard(card, false));
				}else{
					card.setId_prev_card(lastCard.getId_card());
					card.setId_card(bdh.addCard(card, true));
				}
				
				
				
				etNewCardHeader.setText("");
				loadCardList();
		}
		
	}

	private void loadCardList() {
		cardList = bdh.getCardsByIdSpeech(id_speech);
		CardListAdapter adapter = new CardListAdapter(context, R.layout.manage_speech_line, cardList);
		
		if(cardList.size() != 0)
			lastCard = cardList.get(cardList.size()-1);
		
		lvCardList.setAdapter(adapter);
		
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		Log.d(TAG, "RESUME");
		loadCardList();
	}

}
