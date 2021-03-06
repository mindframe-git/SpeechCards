package com.mindframe.speechcards;


import java.util.ArrayList;
import java.util.List;

import com.mindframe.speechcards.model.Card;
import com.mindframe.speechcards.model.Speech;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;


public class SpeechActivity extends Activity {

	TextView tvTitulo, tvCuerpo, tvTamano, btnNext, btnPrev;
	BaseDatosHelper bdh;
	SeekBar sbTamano;
	Context context;
	List<Card> cardList;
	Card currentCard;

	int id_speech, id_card, textSize;
	String bundBody;
	String speechTitle, action;

	final String TAG = getClass().getName();

	public void onCreate(Bundle savedInstanceState) {

		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.card);
		
		context = this.getApplicationContext();

		btnNext = (TextView) findViewById(R.id.btnNext);
		btnPrev = (TextView) findViewById(R.id.btnPrev);
		tvTitulo = (TextView) findViewById(R.id.tvTitulo);
		tvCuerpo = (TextView) findViewById(R.id.tvCuerpo);
		tvTamano = (TextView) findViewById(R.id.tvTamano);
		sbTamano = (SeekBar) findViewById(R.id.sbTamano);

		// Habilitamos el scroll para el textView
		tvCuerpo.setMovementMethod(new ScrollingMovementMethod());

		Typeface font = Typeface.createFromAsset(getAssets(), "FONT.TTF");
		tvTitulo.setTypeface(font);
		tvCuerpo.setTypeface(font);
		tvTamano.setTypeface(font);
		btnNext.setTypeface(font);
		btnPrev.setTypeface(font);

		cardList = new ArrayList<Card>();
		

		bdh = new BaseDatosHelper(context, "SpeechCards", null, 3);

		Bundle bundle = getIntent().getExtras();
		id_speech = bundle.getInt("id_speech");
		speechTitle = bundle.getString("speechTitle");
		id_card = bundle.getInt("id_card");
		bundBody = bundle.getString("body");
		action = bundle.getString("action");

		textSize = bdh.getSpeechById(id_speech).getSize();
		sbTamano.setProgress(textSize);
		tvCuerpo.setTextSize(textSize);

		if (action.compareToIgnoreCase("play") == 0) {
			actionPlay();
		} else if (action.compareToIgnoreCase("preview") == 0) {
			actionPreview();
		}

	}

	private void actionPreview() {
		btnPrev.setText(R.string.btnBack);
		btnNext.setText(R.string.btnMod);

		Card card = new Card();
		card = bdh.getCardById(id_card);
		card.setBody(bundBody);
		
		writeCard(card);
		
//		
//		tvTitulo.setText(currentCard.header);
//		tvCuerpo.setText(bundBody);

		sbTamano.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (textSize == 0)
					textSize = 20;

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
				finish();
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
			if (card.getId_prev_card() == 0)
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
			else
				tvTitulo.setText("");

			if (card.getBody() != null) {
				String body = card.getBody();

				if (body.contains("#") || body.contains("_") || body.contains("%")) {
					tvCuerpo.setText(formatText(body), BufferType.SPANNABLE);
				} else {
					tvCuerpo.setText(body);
				}

			} else {
				tvCuerpo.setText("");
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
				if (card.getId_card() == currentCard.getId_prev_card()) {
					currentCard = card;
					writeCard(card);
					break;
				}
			}
		}
	}
}
