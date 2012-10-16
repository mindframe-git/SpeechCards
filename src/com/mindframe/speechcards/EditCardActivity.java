package com.mindframe.speechcards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditCardActivity extends Activity{

	TextView tvTitleSpeech;
	EditText etHeader, etBody;
	Button btnBack, btnPreview;
	
	Card currentCard = new Card();
	int id_card;
	String speechTitle;
	Context context;
	BaseDatosHelper bdh;
	
	final String TAG = getClass().getName();

	public void onCreate(Bundle savedInstanceState) {

		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.editcard);
		context = this.getApplicationContext();
		
		tvTitleSpeech = (TextView)findViewById(R.id.tvTitleSpeech);
		etHeader = (EditText)findViewById(R.id.etHeader);
		etBody = (EditText)findViewById(R.id.etBody);
		btnBack = (Button)findViewById(R.id.btnBack);
		btnPreview = (Button)findViewById(R.id.btnPreview);
		
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 2);
		
		Bundle bundle = getIntent().getExtras();
		id_card = bundle.getInt("id_card");
		speechTitle = bundle.getString("speechTitle");
		
		tvTitleSpeech.setText(speechTitle);
		
		currentCard = bdh.getCardById(id_card);
		
		etHeader.setText(currentCard.header);
		etBody.setText(currentCard.body);
		
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bdh.updateCard(currentCard, etHeader.getText().toString(), etBody.getText().toString());
				finish();
				
			}
		});
		
		btnPreview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Mandaremos a la vista previa donde podremos cambiar el tamaño de la letra.
//				
//				id_speech = bundle.getInt("id_speech");
//				speechTitle = bundle.getString("speechTitle");
//				mode = bundle.getString("mode");
				
				Bundle bun = new Bundle();
				bun.putInt("id_speech", currentCard.id_speech);
				bun.putString("speechTitle", speechTitle);
				bun.putInt("id_card", id_card);
				bun.putString("action", "preview");
				bun.putString("body", etBody.getText().toString());
				Intent intent = new Intent(EditCardActivity.this, SpeechActivity.class);
				
				intent.putExtras(bun);
				startActivity(intent);
				
			}
		});
		
	}
	
}
