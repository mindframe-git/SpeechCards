package com.mindframe.speechcards;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class SpeechActivity extends Activity {

	Button btnNext, btnPrev;
	TextView tvTitulo, tvCuerpo;
	BaseDatosHelper bdh;
	Context context;
	List<Card> cardList;
	Card currentCard;
	
	int id_speech;
	String speechTitle;
	
	final String TAG = getClass().getName();
		
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
		
		//Habilitamos el scroll para el textView
		tvCuerpo.setMovementMethod(new ScrollingMovementMethod());
		
		cardList = new ArrayList<Card>();
		context = this.getApplicationContext();
		
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 1);
		
		Bundle bundle = getIntent().getExtras();
		id_speech =  bundle.getInt("id_speech");
		speechTitle = bundle.getString("speechTitle");
		
		cardList = bdh.getCardsByIdSpeech(id_speech);
		
		if(cardList.isEmpty()){
			Toast.makeText(context, R.string.toastNoCards, Toast.LENGTH_SHORT).show();
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
		
	}

	private Card getFirstCard() {
		for(Card card : cardList){
			if(card.id_prev_card == 0)
				return card;
		}
		return null;
	}
	
	private void writeCard(Card card) {
		//Al cargar la tarjeta ponemos el scroll al principio.
		tvCuerpo.scrollTo(0, 0);
		
		if(card != null){
			if(card.getHeader() != null)
				tvTitulo.setText(card.getHeader());
			if(card.getBody() != null){
				String body = card.getBody();
				
				if(body.contains("#") || body.contains("_")){
					tvCuerpo.setText(formatText(body),BufferType.SPANNABLE);
				}else{
					tvCuerpo.setText(body);
				}
				
			}
		}
	}
	
	private Spanned formatText(String text){
		
		boolean openBold = true;
		boolean openUnderLine = true;
		boolean openItalic = true;
		
		//Buscamos los caracteres especiales 
		//y los sustituimos por el código html
		for(int i = 0; i< text.length(); i++){
			String x = text.substring(i, i+1);
			if(x.compareToIgnoreCase("#")==0){
				
				if(openBold)
					text = text.replaceFirst("#", "<b>");
				else
					text = text.replaceFirst("#", "</b>");
				
				openBold = !openBold;
				
			}
			if(x.compareToIgnoreCase("_")==0){
				
				if(openUnderLine)
					text = text.replaceFirst("_", "<u>");
				else
					text = text.replaceFirst("_", "</u>");
				
				openUnderLine = !openUnderLine;
			}
			if(x.compareToIgnoreCase("\n")==0){
				text = text.replaceFirst("\n", "<br>");
			}
			if(x.compareToIgnoreCase("/")==0){
				
				if(openItalic)
					text = text.replaceFirst("%", "<i>");
				else
					text = text.replaceFirst("%", "</i>");
				
				openItalic = !openItalic;
			}
		}
		
		Spanned span = Html.fromHtml(text);
		return span;
	}
	
	private void getNextCard(){
		if(currentCard.isLast()){
			Toast.makeText(context, R.string.toastNoMoreCards, Toast.LENGTH_SHORT).show();
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
			Toast.makeText(context,  R.string.toastFisrtCard, Toast.LENGTH_SHORT).show();
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.speech_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnEditSpeech:
			//redirigimos a la página de edición de este menú
			Bundle bun = new Bundle();
			bun.putInt("id_speech", id_speech);
			bun.putString("speechTitle", speechTitle);
			Intent intent = new Intent(SpeechActivity.this, EditSpeechActivity.class);
			intent.putExtras(bun);
			startActivity(intent);
			return true;
		
		default:
			return super.onOptionsItemSelected(item);

		}
	}
	
	protected void onResume(){
		super.onResume();
		
		Log.d(TAG, "onResume: id: " + id_speech);
		cardList = bdh.getCardsByIdSpeech(id_speech);

		if(cardList.isEmpty()){
			Toast.makeText(context, R.string.toastNoCards, Toast.LENGTH_SHORT).show();
			finish();
		}
		
		currentCard = getFirstCard();
		writeCard(currentCard);
	}
	
}
