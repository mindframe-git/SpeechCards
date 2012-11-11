package com.mindframe.speechcards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindframe.speechcards.BaseDatosHelper;
import com.mindframe.speechcards.R;
import com.mindframe.speechcards.model.Card;

public class EditCardActivity extends Activity{

	TextView tvTitleSpeech,textView1, textView2, btnPreview;
	EditText etHeader, etBody;
	ImageView btnBack;
	
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
		textView1 = (TextView)findViewById(R.id.textView1);
		textView2 = (TextView)findViewById(R.id.textView2);
		etHeader = (EditText)findViewById(R.id.etHeader);
		etBody = (EditText)findViewById(R.id.etBody);
		btnBack = (ImageView)findViewById(R.id.btnBack);
		btnPreview = (TextView)findViewById(R.id.btnPreview);
		
		Typeface font = Typeface.createFromAsset(context.getAssets(), "FONT.TTF");
		tvTitleSpeech.setTypeface(font);
		etHeader.setTypeface(font);
		etBody.setTypeface(font);
		textView1.setTypeface(font);
		textView2.setTypeface(font);
		btnPreview.setTypeface(font);
		
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 3);
		
		Bundle bundle = getIntent().getExtras();
		id_card = bundle.getInt("id_card");
		speechTitle = bundle.getString("speechTitle");
		
		tvTitleSpeech.setText(speechTitle);
		
		currentCard = bdh.getCardById(id_card);
		
		etHeader.setText(currentCard.getHeader());
		etBody.setText(currentCard.getBody());
		
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentCard.setBody(etBody.getText().toString());
				currentCard.setHeader(etHeader.getText().toString());
				bdh.updateCard(currentCard);
				finish();
				
			}
		});
		
		btnPreview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Mandaremos a la vista previa donde podremos cambiar el tamaño de la letra.
				
				Bundle bun = new Bundle();
				bun.putInt("id_speech", currentCard.getId_speech());
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
	
	//Al pulsar back se guarda la tarjeta, pero creo que no es buena idea...
//	@Override
//	public void onDestroy(){
//		super.onDestroy();
//		
//		bdh.updateCard(currentCard, etHeader.getText().toString(), etBody.getText().toString());
//	}
	
}
