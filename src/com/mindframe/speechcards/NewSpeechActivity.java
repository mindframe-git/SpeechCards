package com.mindframe.speechcards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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

	ImageView btnNewSpeech;
	EditText etNewSpeech;
	Context context;
	BaseDatosHelper bdh;
	final String TAG = getClass().getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		context = this.getApplicationContext();
		setContentView(R.layout.new_speech);

		bdh = new BaseDatosHelper(context, "SpeechCards", null, 1);
		btnNewSpeech = (ImageView) findViewById(R.id.btnNewSpeech);
		etNewSpeech = (EditText) findViewById(R.id.etNewSpeech);

		btnNewSpeech.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String speechTitle = etNewSpeech.getText().toString();
				if (speechTitle == null || speechTitle.trim().compareToIgnoreCase("") == 0) {
					Toast.makeText(context, R.string.toastVoidName, Toast.LENGTH_SHORT).show();
				} else {
					if (!bdh.existsSpeech(speechTitle)) {
						int id_speech;
						Bundle bun = new Bundle();

						id_speech = bdh.newSpeech(speechTitle);

						Intent intent = new Intent(NewSpeechActivity.this, EditSpeechActivity.class);
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
		});
	}
	
	
	@Override
	public void onStop(){
		super.onStop();
		Log.d(TAG, "onStop");
		finish();
	}
}
