package com.mindframe.speechcards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author mindframe
 * 
 * 
 *         Esta actividad se dedica a coger crear una entrada de base de datos
 *         con el nombre del discurso. Si ya existe, se muestra un mensaje de
 *         error. Si está vacío se avisa. Si todo está correcto, se pasa a la
 *         nueva actividad para añadir tarjetas.
 */

public class NewSpeechActivity extends Activity {

	EditText etNewSpeech;
	TextView tvTitle, tvNew, btnNewSpeech;
	Context context;
	int textSize;
	BaseDatosHelper bdh;
	
	
	final String TAG = getClass().getName();
	boolean grabado;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		context = this.getApplicationContext();
		setContentView(R.layout.new_speech);
		
		//El tamaño de texto predeterminado es 20
		textSize = 20;

		bdh = new BaseDatosHelper(context, "SpeechCards", null, 2);
		btnNewSpeech = (TextView) findViewById(R.id.btnNewSpeech);
		etNewSpeech = (EditText) findViewById(R.id.etNewSpeech);
		tvTitle = (TextView)findViewById(R.id.tvTitle);
		tvNew = (TextView)findViewById(R.id.tvNew);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "FONT.TTF");
		tvTitle.setTypeface(font);
		tvNew.setTypeface(font);
		btnNewSpeech.setTypeface(font);
		etNewSpeech.setTypeface(font);
		
		
		tvTitle.setText(R.string.tvNewCollecion);
		
		
		// Al pulsar enter, se crea la colección
		grabado = false;
		etNewSpeech.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					if(!grabado){
						nuevaColeccion();
						grabado = true;
						return true;
					}
				}
				return false;
			}
		});

		btnNewSpeech.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nuevaColeccion();
			}
		});
		
	}

	public void nuevaColeccion() {
		String speechTitle = etNewSpeech.getText().toString();
		if (speechTitle == null || speechTitle.trim().compareToIgnoreCase("") == 0) {
			Toast.makeText(context, R.string.toastVoidName, Toast.LENGTH_SHORT).show();
		} else {
			if (!bdh.existsSpeech(speechTitle)) {
				int id_speech;
				Bundle bun = new Bundle();

				id_speech = bdh.newSpeech(speechTitle, textSize);

				Intent intent = new Intent(NewSpeechActivity.this, ManageSpeechActivity.class);
				bun.putString("speechTitle", speechTitle);
				bun.putInt("id_speech", id_speech);
				bun.putInt("id_prev_card", -1);
				intent.putExtras(bun);

				startActivity(intent);

			} else {
				Toast.makeText(context, R.string.toastExistsSpeech, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
		finish();
	}

}
