package com.mindframe.speechcards;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SpeechActivity extends Activity {

	Button btnNext, btnPrev;
	TextView tvTitulo, tvCuerpo;
	BaseDatosHelper bdh;
	Context context;
	List<Card> cardList;
	Card currentCard;
		
	public void onCreate(Bundle savedInstanceState) {

		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.card);
		
		btnNext = (Button)findViewById(R.id.btnNext);
		btnPrev = (Button)findViewById(R.id.btnPrev);
		tvTitulo = (TextView)findViewById(R.id.tvTitulo);
		tvCuerpo = (TextView)findViewById(R.id.tvCuerpo);
		
		cardList = new ArrayList<Card>();
		context = this.getApplicationContext();
		
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 1);
		
		Bundle bundle = getIntent().getExtras();
		int id_speech =  bundle.getInt("id_speech");
		
		cardList = bdh.getCardsByIdSpeech(id_speech);
		
		if(cardList.isEmpty()){
			Toast.makeText(context, "El discurso no tiene tarjetas.", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		currentCard = getFirstCard();
		writeCard(currentCard);
		
		
		btnPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getPrevCard();
				writeCard(currentCard);
			}

		});
		
		
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getNextCard();
				writeCard(currentCard);
			}
		});
		
//		Toast.makeText(context, "Se han encontrado " + cardList.size() + " tarjetas", Toast.LENGTH_SHORT).show();
		
	}

	private Card getFirstCard() {
		for(Card card : cardList){
			if(card.id_prev_card == 0)
				return card;
		}
		return null;
	}
	
	private void writeCard(Card card) {
		if(card != null){
			if(card.getHeader() != null)
				tvTitulo.setText(card.getHeader());
			if(card.getBody() != null)
				tvCuerpo.setText(card.getBody());
		}
	}
	
	private void getNextCard(){
		if(currentCard.isLast()){
			Toast.makeText(context, "No hay más tarjetas.", Toast.LENGTH_SHORT).show();
		}else{
			for(Card card : cardList){
				if(card.getId_card() == currentCard.getId_next_card()){
					currentCard = card;
					writeCard(card);
					break;
				}
			}
		}
	}
	
	private void getPrevCard(){
		if(currentCard.isFirst()){
			Toast.makeText(context, "Se encuentra en la primera tarjeta.", Toast.LENGTH_SHORT).show();
		}else{
			for(Card card : cardList){
				if(card.id_card == currentCard.getId_prev_card()){
					currentCard = card;
					writeCard(card);
					break;
				}
			}
		}
	}
	
}
