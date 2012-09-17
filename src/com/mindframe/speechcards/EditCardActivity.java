package com.mindframe.speechcards;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditCardActivity extends Activity {

	TextView tvTitleSpeech;
	EditText etHeader, etBody;
	Button btnPrev, btnAddCard, btnDeleteCard, btnEnd;
	BaseDatosHelper bdh;
	Context context;
	int id_prev_card;
	final String TAG = getClass().getName();

	public void onCreate(Bundle savedInstanceState) {
		
		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_card);
		
		context = this.getApplicationContext();
		tvTitleSpeech = (TextView) findViewById(R.id.tvTitleSpeech);
		etHeader = (EditText) findViewById(R.id.etHeader);
		etBody = (EditText) findViewById(R.id.etBody);
		btnAddCard = (Button)findViewById(R.id.btnAddCard);
		btnEnd = (Button)findViewById(R.id.btnEnd);
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 1);

		Bundle bundle = getIntent().getExtras();

		String speechTitle = bundle.getString("newSpeech");
		final int id_speech = bundle.getInt("id_speech");
		final int id_prev = bundle.getInt("id_prev_card");
		if(id_prev == -1){
			id_prev_card = 0;
		}
		Log.d(TAG, "Title: " + speechTitle + " id: " +id_speech);
		tvTitleSpeech.setText(speechTitle);
				
		btnAddCard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(etBody.getText().toString().trim().compareToIgnoreCase("") != 0){
					addCard(etHeader.getText().toString(), etBody.getText().toString(), id_speech);
					
					etHeader.setText("");
					etBody.setText("");
					
					Toast.makeText(context, "Tarjeta agregada", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(context, "Debe escribir algo en el cuerpo.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnEnd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}

	public int addCard(String header, String body, int id_speech) {
		
		int idNewCard = 0;
		boolean actCard = false;
		
		Card card = new Card();
		
		card.header = header;
		card.body = body;
		card.id_speech = id_speech;
		card.id_prev_card = id_prev_card;
		//La última tarjeta tendrá id_next_card = 0;
		card.id_next_card = 0;

		if(id_prev_card != 0){
			actCard = true;
		}
		
		idNewCard = bdh.addCard(card, actCard);
		id_prev_card = idNewCard;
		return idNewCard;
	}

}
