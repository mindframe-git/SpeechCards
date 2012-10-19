package com.mindframe.speechcards;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
 *         Esta clase cargará en la plantilla de edición las tarjetas
 *         pertenecientes a un discurso, puede navegar entre ellas, añadir,
 *         modificar y borrar tarjetas y discursos.
 * 
 */
public class EditSpeechActivity extends Activity {

	List<Card> cardList;
	Card currentCard;
	Context context;
	BaseDatosHelper bdh;
	TextView tvTitleSpeech;
	EditText etHeader, etBody;
	Button btnPrevCard, btnNextCard, btnMod;
	int id_speech;
//	RelativeLayout pantalla;
	boolean addingCard;
	
	final String TAG = getClass().getName();

	public void onCreate(Bundle savedInstanceState) {

		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.edit_speech);
		
		tvTitleSpeech = (TextView) findViewById(R.id.tvTitleSpeech);
		etHeader = (EditText) findViewById(R.id.etHeader);
		etBody = (EditText) findViewById(R.id.etBody);
		btnNextCard = (Button) findViewById(R.id.btnNextCard);
		btnPrevCard = (Button) findViewById(R.id.btnPrevCard);
		btnMod = (Button) findViewById(R.id.btnMod);

		cardList = new ArrayList<Card>();
		context = this.getApplicationContext();

		bdh = new BaseDatosHelper(context, "SpeechCards", null, 2);
		addingCard = false;

		Bundle bundle = getIntent().getExtras();

		id_speech = bundle.getInt("id_speech");
		tvTitleSpeech.setText(bundle.getString("speechTitle"));

		cardList = bdh.getCardsByIdSpeech(id_speech);

		if (cardList.isEmpty()) {
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

				if (addingCard) {
					Card card = new Card();
					card.body = etBody.getText().toString();
					card.header = etHeader.getText().toString();
					card.id_prev_card = currentCard.id_card;
					card.id_next_card = currentCard.id_next_card;
					card.id_speech = id_speech;
					if (etBody.getText().toString().trim().compareToIgnoreCase("") != 0) {
						int idNewCard = bdh.addCard(card, true);
						addingCard = false;
						Toast.makeText(context, R.string.toastAddCard, Toast.LENGTH_SHORT).show();

						cardList = bdh.getCardsByIdSpeech(id_speech);

						currentCard = getCardById(idNewCard);
					} else {
						Toast.makeText(context, R.string.toastVoidBody, Toast.LENGTH_SHORT).show();
					}

				} else {
					if (currentCard != null && !addingCard) {
						if (etBody.getText().toString().trim().compareToIgnoreCase("") != 0) {
							if (bdh.updateCard(currentCard, etHeader.getText().toString(), etBody.getText().toString())) {
								Toast.makeText(context, R.string.toastUpdateCard, Toast.LENGTH_SHORT).show();
								cardList = bdh.getCardsByIdSpeech(id_speech);
							} else {
								Toast.makeText(context, R.string.toastFail, Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(context, R.string.toastVoidBody, Toast.LENGTH_SHORT).show();
						}
					} else {
						// No hay tarjetas y vamos a añadir la primera.

						if (etBody.getText().toString().trim().compareToIgnoreCase("") != 0) {
							Card newCard = new Card();
							newCard.id_speech = id_speech;
							newCard.id_next_card = 0;
							newCard.id_prev_card = 0;
							newCard.body = etBody.getText().toString();
							newCard.header = etHeader.getText().toString();

							bdh.addCard(newCard, false);

							cardList = bdh.getCardsByIdSpeech(id_speech);
							currentCard = getFirstCard();

							Toast.makeText(context, R.string.toastAddCard, Toast.LENGTH_SHORT).show();

						} else {
							Toast.makeText(context, R.string.toastVoidBody, Toast.LENGTH_SHORT).show();
						}
					}
				}

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
		if (card != null) {
			if (card.getHeader() != null)
				etHeader.setText(card.getHeader());
			if (card.getBody() != null)
				etBody.setText(card.getBody());
		}
	}

	private void getNextCard() {
		if (currentCard != null) {
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
		} else {
			Toast.makeText(context, R.string.toastNoCards, Toast.LENGTH_SHORT).show();
		}

	}

	private void getPrevCard() {
		if (currentCard != null) {
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
		} else {
			Toast.makeText(context, R.string.toastNoCards, Toast.LENGTH_SHORT).show();
		}
	}

	public Card getCardById(int idCard) {

		for (Card card : cardList) {
			if (card.id_card == idCard) {
				return card;
			}
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.menu_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.mnDelSpeech:
			crearDialogoConfirmacion();
			return true;

		case R.id.mnAddCard:
			etBody.setText("");
			etHeader.setText("");
			addingCard = true;
			return true;

		case R.id.mnDelCard:
			if (currentCard != null) {
				int result = bdh.delCard(currentCard);

				if (result != 1) {
					Toast.makeText(context, R.string.toastFail, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, R.string.toastDelCard, Toast.LENGTH_SHORT).show();

					if (currentCard.isFirst() && currentCard.isLast()) {
						// Es la última tarjeta
						Toast.makeText(context, R.string.toastNoMoreCards, Toast.LENGTH_SHORT).show();
						etBody.setText("");
						etHeader.setText("");
						currentCard = null;
					} else {

						cardList = bdh.getCardsByIdSpeech(id_speech);

						if (currentCard.isLast())
							getPrevCard();
						if (currentCard.isFirst())
							getNextCard();
						if (!currentCard.isFirst() && !currentCard.isLast())
							getPrevCard();
					}
				}
			} else {
				Toast.makeText(context, R.string.toastNoCards, Toast.LENGTH_SHORT).show();
			}
			return true;

		case R.id.mnHelp:
			// Mostraremos página de ayuda e instrucciones

			Dialog dialog = new Dialog(EditSpeechActivity.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.help);
			dialog.setCancelable(true);

			TextView text = (TextView) dialog.findViewById(R.id.tvHelp);
			text.setText(Html.fromHtml(getString(R.string.tvHelp)));
			text.setMovementMethod(new ScrollingMovementMethod());

			dialog.show();

			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}

	private Dialog crearDialogoConfirmacion() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialTitle);
		builder.setMessage(R.string.dialMessageSpeech);
		builder.setPositiveButton(R.string.dialAcept, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				bdh.deleteSpeech(id_speech);
				Toast.makeText(context, R.string.toastDelSpeech, Toast.LENGTH_SHORT).show();
				dialog.cancel();
				finish();
			}
		});
		builder.setNegativeButton(R.string.dialCancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		return builder.show();
	}

}
