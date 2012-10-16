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
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class SpeechActivity extends Activity{

	Button btnNext, btnPrev;
	TextView tvTitulo, tvCuerpo, tvTamano;
	BaseDatosHelper bdh;
	SeekBar sbTamano;
	Context context;
	List<Card> cardList;
	Card currentCard;

	int id_speech, id_card, textSize;
	String body;
	String speechTitle, action;

	final String TAG = getClass().getName();


	public void onCreate(Bundle savedInstanceState) {

		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.card);
		
		
		btnNext = (Button) findViewById(R.id.btnNext);
		btnPrev = (Button) findViewById(R.id.btnPrev);
		tvTitulo = (TextView) findViewById(R.id.tvTitulo);
		tvCuerpo = (TextView) findViewById(R.id.tvCuerpo);
		tvTamano = (TextView)findViewById(R.id.tvTamano);
		sbTamano = (SeekBar)findViewById(R.id.sbTamano);
		
		// Habilitamos el scroll para el textView
		tvCuerpo.setMovementMethod(new ScrollingMovementMethod());

		
		cardList = new ArrayList<Card>();
		context = this.getApplicationContext();

		bdh = new BaseDatosHelper(context, "SpeechCards", null, 2);

		Bundle bundle = getIntent().getExtras();
		id_speech = bundle.getInt("id_speech");
		speechTitle = bundle.getString("speechTitle");
		id_card = bundle.getInt("id_card");
		body = bundle.getString("body");
		action = bundle.getString("action");
		
		textSize = bdh.getSpeechById(id_speech).size;
		sbTamano.setProgress(textSize);
		tvCuerpo.setTextSize(textSize);
		
		if(action.compareToIgnoreCase("play") == 0){
			actionPlay();
		}else if(action.compareToIgnoreCase("preview") == 0){
			actionPreview();
		}
		
		

	}

	private void actionPreview() {
		btnPrev.setText(R.string.btnBack);
		btnNext.setText(R.string.btnMod);
		
		currentCard = bdh.getCardById(id_card);
		currentCard.body = body;
		writeCard(currentCard);
		
		
		sbTamano.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(textSize == 0)
					textSize=20;
				textSize = progress;
				tvCuerpo.setTextSize(textSize);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
		
		});
		
		btnPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Speech speech = new Speech();
				speech = bdh.getSpeechById(id_speech);
				speech.setSize(textSize);
				bdh.updateSpeech(speech);
				
				Toast.makeText(context, R.string.toastUpdateSize, Toast.LENGTH_SHORT).show();
				
			}
		});
		
		
		
	}

	private void actionPlay() {
		
		sbTamano.setVisibility(View.GONE);
		tvTamano.setVisibility(View.GONE);
		
		cardList = bdh.getCardsByIdSpeech(id_speech);

		if (cardList.isEmpty()) {
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
		for (Card card : cardList) {
			if (card.id_prev_card == 0)
				return card;
		}
		return null;
	}

	private void writeCard(Card card) {
		// Al cargar la tarjeta ponemos el scroll al principio.
		tvCuerpo.scrollTo(0, 0);
		
		if (card != null) {
			if (card.getHeader() != null)
				tvTitulo.setText(card.getHeader());
			if (card.getBody() != null) {
				String body = card.getBody();

				if (body.contains("#") || body.contains("_") || body.contains("%")) {
					tvCuerpo.setText(formatText(body), BufferType.SPANNABLE);
				} else {
					tvCuerpo.setText(body);
				}

			}
		}
	}

	private Spanned formatText(String text) {

		boolean openBold = true;
		boolean openUnderLine = true;
		boolean openItalic = true;

		// Buscamos los caracteres especiales
		// y los sustituimos por el código html
		for (int i = 0; i < text.length(); i++) {
			String x = text.substring(i, i + 1);
			if (x.compareToIgnoreCase("#") == 0) {

				if (openBold)
					text = text.replaceFirst("#", "<b>");
				else
					text = text.replaceFirst("#", "</b>");

				openBold = !openBold;

			}
			if (x.compareToIgnoreCase("_") == 0) {

				if (openUnderLine)
					text = text.replaceFirst("_", "<u>");
				else
					text = text.replaceFirst("_", "</u>");

				openUnderLine = !openUnderLine;
			}
			if (x.compareToIgnoreCase("\n") == 0) {
				text = text.replaceFirst("\n", "<br>");
			}
			if (x.compareToIgnoreCase("/") == 0) {

				if (openItalic)
					text = text.replaceFirst("%", "<i>");
				else
					text = text.replaceFirst("%", "</i>");

				openItalic = !openItalic;
			}
		}

		Spanned span = Html.fromHtml(text);
		return span;
	}

	private void getNextCard() {
		if (currentCard.isLast()) {
			Toast.makeText(context, R.string.toastNoMoreCards, Toast.LENGTH_SHORT).show();
		} else {
			for (Card card : cardList) {
				if (card.getId_card() == currentCard.getId_next_card()) {
					currentCard = card;
					writeCard(card);
					break;
				}
			}
		}
	}

	private void getPrevCard() {
		if (currentCard.isFirst()) {
			Toast.makeText(context, R.string.toastFisrtCard, Toast.LENGTH_SHORT).show();
		} else {
			for (Card card : cardList) {
				if (card.id_card == currentCard.getId_prev_card()) {
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
			// redirigimos a la página de edición de este menú
			Bundle bun = new Bundle();
			bun.putInt("id_card", currentCard.id_card);
			bun.putString("speechTitle", speechTitle);
			Intent intent = new Intent(SpeechActivity.this, EditCardActivity.class);
			intent.putExtras(bun);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);

		}
	}

//	protected void onResume() {
//		super.onResume();
//
//		Log.d(TAG, "onResume: id: " + id_speech);
//		cardList = bdh.getCardsByIdSpeech(id_speech);
//
//		if (cardList.isEmpty()) {
//			Toast.makeText(context, R.string.toastNoCards, Toast.LENGTH_SHORT).show();
//			finish();
//		}
//
//		currentCard = getFirstCard();
//		writeCard(currentCard);
//	}
}
