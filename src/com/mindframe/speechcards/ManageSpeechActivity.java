package com.mindframe.speechcards;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
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
		
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 2);
		
		btnAddCard = (ImageView)findViewById(R.id.btnAddCard);
		etNewCardHeader = (EditText)findViewById(R.id.etNewCardHeader);
		lvCardList = (ListView)findViewById(R.id.lvCardList);
		
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
				
//		lvCardList.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, final long arg3) {
//				
//				Intent intent = new Intent(ManageSpeechActivity.this, EditCardActivity.class);
//				Bundle bun = new Bundle();
//				bun.putInt("id_card", cardList.get(arg2).getId_card());
//				bun.putString("speechTitle", speechTitle);
//				
//				intent.putExtras(bun);
//				startActivity(intent);
//				
//				ImageView btnRemove = (ImageView) arg1.findViewById(R.id.btnRemove);
//				btnRemove.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						Log.d(TAG, "arg2: " +arg2+ " arg3: " + arg3);
//						
//						crearDialogoConfirmacion(arg2);
//						
//					}
//				});
//			}
//		});
		
	}

	protected void addCard() {
		//Controlar cabecera vacía:
		
		if(etNewCardHeader.getText().toString().trim().compareToIgnoreCase("") == 0){
			Toast.makeText(context, R.string.toastVoidHeader, Toast.LENGTH_SHORT).show();
		}else{
				Card card = new Card();
				card.header = etNewCardHeader.getText().toString().trim();
				card.id_next_card = 0;
				card.id_speech = id_speech;
				if(cardList.size() == 0){
					card.id_prev_card = 0;
					card.id_card = bdh.addCard(card, false);
				}else{
					card.id_prev_card = lastCard.id_card;
					card.id_card = bdh.addCard(card, true);
				}
				
				
				
				etNewCardHeader.setText("");
				loadCardList();
		}
		
	}

	private void loadCardList() {
		cardList = bdh.getCardsByIdSpeech(id_speech);
		CardListAdapter adapter = new CardListAdapter(context, R.layout.cardline, cardList);
		
		if(cardList.size() != 0)
			lastCard = cardList.get(cardList.size()-1);
		
		lvCardList.setAdapter(adapter);
		
		
	}

//	private Dialog crearDialogoConfirmacion(final int position) {
//
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle(R.string.dialTitle);
//		builder.setMessage(R.string.dialMessage);
//		builder.setPositiveButton(R.string.dialAcept, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				bdh.delCard(cardList.get(position));
//				Toast.makeText(context, R.string.toastDelCard, Toast.LENGTH_SHORT).show();
//				loadCardList();
//				dialog.cancel();
//			}
//		});
//		builder.setNegativeButton(R.string.dialCancel, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.cancel();
//			}
//		});
//		
//		return builder.show();
//	}
	
	@Override
	public void onResume(){
		super.onResume();
		Log.d(TAG, "RESUME");
		loadCardList();
	}

}
