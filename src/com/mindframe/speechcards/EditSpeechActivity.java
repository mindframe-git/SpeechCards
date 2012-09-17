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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author mindframe
 *
 *Esta clase cargará en la plantilla de edición las tarjetas pertenecientes a un discurso,
 *puede navegar entre ellas, añadir, modificar y borrar tarjetas.
 *
 */
public class EditSpeechActivity extends Activity {
	
	List<Card> cardList;
	Card currentCard;
	Context context; 
	BaseDatosHelper bdh;
	TextView tvTitleSpeech;
	EditText etHeader, etBody;
	Button btnPrevCard, btnNextCard, btnAdd, btnDeleteCard, btnMod;
	int id_speech;
	boolean addingCard;
	
	
	public void onCreate(Bundle savedInstanceState) {
		
		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_speech);
		
		
		tvTitleSpeech = (TextView)findViewById(R.id.tvTitleSpeech);
		etHeader = (EditText)findViewById(R.id.etHeader);
		etBody = (EditText)findViewById(R.id.etBody);
		btnNextCard = (Button)findViewById(R.id.btnNextCard);
		btnPrevCard = (Button)findViewById(R.id.btnPrevCard);
		btnMod = (Button)findViewById(R.id.btnMod);
		btnDeleteCard = (Button)findViewById(R.id.btnDeleteCard);
		btnAdd = (Button)findViewById(R.id.btnAdd);
		
		cardList = new ArrayList<Card>();
		context = this.getApplicationContext();
		
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 1);
		addingCard = false;
		
		
		Bundle bundle = getIntent().getExtras();
		
		id_speech =  bundle.getInt("id_speech");
		tvTitleSpeech.setText(bundle.getString("speechTitle"));
		
		cardList = bdh.getCardsByIdSpeech(id_speech);
		
		if(cardList.isEmpty()){
//			Toast.makeText(context, "El discurso no tiene tarjetas.", Toast.LENGTH_SHORT).show();
//			finish();
			currentCard = null;
			etHeader.setText("");
			etBody.setText("");
		}
		
		currentCard = getFirstCard();
		
		writeCard(currentCard);
		
		
		btnPrevCard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getPrevCard();
				
			}
		});
		
		
		btnNextCard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getNextCard();
				
			}
		});
		
		btnMod.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(addingCard){
					Card card = new Card();
					card.body = etBody.getText().toString();
					card.header = etHeader.getText().toString();
					card.id_prev_card = currentCard.id_card;
					card.id_next_card = currentCard.id_next_card;
					card.id_speech = id_speech;
					if(etBody.getText().toString().trim().compareToIgnoreCase("") != 0){
						int idNewCard = bdh.addCard(card, true);
						addingCard = false;
						Toast.makeText(context, "Se ha añadido la tarjeta.", Toast.LENGTH_SHORT).show();
						
						cardList = bdh.getCardsByIdSpeech(id_speech);
						
						currentCard = getCardById(idNewCard);
					}else{
						Toast.makeText(context, "Debe escribir algo en el cuerpo.", Toast.LENGTH_SHORT).show();
					}
					
				}else{
					if(currentCard != null && !addingCard){
						if(etBody.getText().toString().trim().compareToIgnoreCase("") != 0){
							if(bdh.modCard(currentCard, etHeader.getText().toString(), etBody.getText().toString())){
								Toast.makeText(context, "Se ha actualizado la tarjeta.", Toast.LENGTH_SHORT).show();
								cardList = bdh.getCardsByIdSpeech(id_speech);
							}else{
								Toast.makeText(context, "Algo ha fallado :S", Toast.LENGTH_SHORT).show();
							}
						}else{
							Toast.makeText(context, "Debe escribir algo en el cuerpo.", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(context, "No hay tarjetas", Toast.LENGTH_SHORT).show();
					}
				}
				
			}
		});
		
		btnDeleteCard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(currentCard != null){
					int result = bdh.delCard(currentCard);
					
					if(result != 1){
						Toast.makeText(context, "Algo ha fallado :S", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(context, "Tarjeta borrada.", Toast.LENGTH_SHORT).show();
						
						if(currentCard.isFirst() && currentCard.isLast()){
							//Es la última tarjeta
							Toast.makeText(context, "No hay más tarjetas.", Toast.LENGTH_SHORT).show();
							etBody.setText("");
							etHeader.setText("");
							currentCard = null;
						}else{
							
							cardList = bdh.getCardsByIdSpeech(id_speech);
							
							if(currentCard.isLast())
								getPrevCard();
							if(currentCard.isFirst())
								getNextCard();
							if(!currentCard.isFirst() && !currentCard.isLast())
								getPrevCard();
						}
						
						
					}
				}else
					Toast.makeText(context, "No hay más tarjetas.", Toast.LENGTH_SHORT).show();
			}
		});
		
		btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*
				 * Varios escenarios:
				 * 1) No hay tarjeta.
				 * 2) Es una tarjeta de enmedio
				 */
				
				//Escenario: No hay ninguna tarjeta:
				//la pantalla estará en blanco, se agregara con prev y next a 0
				//se establecerá esta nueva a currentCard y se recargará la lista.
				if(currentCard == null){
					if(etBody.getText().toString().trim().compareToIgnoreCase("") != 0){
						Card newCard = new Card();
						newCard.id_speech = id_speech;
						newCard.id_next_card = 0;
						newCard.id_prev_card = 0;
						newCard.body = etBody.getText().toString();
						newCard.header = etHeader.getText().toString();
						
						bdh.addCard(newCard, false);
						
						cardList = bdh.getCardsByIdSpeech(id_speech);
						currentCard = getFirstCard();
						
						Toast.makeText(context, "Tarjeta agregada.", Toast.LENGTH_SHORT).show();
						
					}else{
						Toast.makeText(context, "Debe escribir algo en el cuerpo.", Toast.LENGTH_SHORT).show();
					}
				}else{
					/*
					 * Escenario: Es una tarjeta de enmedio
					 * Se ponen los campos en blanco.
					 * newCard.prev = curr.id
					 * newCard.next = curr.next
					 */
					etBody.setText("");
					etHeader.setText("");
					addingCard = true;
					
					Toast.makeText(context, "Pulse modificar para terminar.", Toast.LENGTH_SHORT).show();
				}
			}
		});
	
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
				etHeader.setText(card.getHeader());
			if(card.getBody() != null)
				etBody.setText(card.getBody());
		}
	}
	
	private void getNextCard(){
		if(currentCard != null){
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
		}else{
			Toast.makeText(context, "No hay tarjetas.", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private void getPrevCard(){
		if(currentCard != null){
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
		}else{
			Toast.makeText(context, "No hay tarjetas.", Toast.LENGTH_SHORT).show();
		}
	}
	public Card getCardById(int idCard){
		
		for(Card card : cardList){
			if(card.id_card == idCard){
				return card;
			}
		}
		return null;
	}
}
