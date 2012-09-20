package com.mindframe.speechcards;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends Activity {

	Button btnEditSpeech, btnPlaySpeech, btnNewSpeech;
	Spinner spSpeechs;
	BaseDatosHelper bdh;
	Context context;

	List<Speech> speechList = new ArrayList<Speech>();
	boolean permiso = false;

	final String TAG = getClass().getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);

		context = this.getApplicationContext();
		btnPlaySpeech = (Button) findViewById(R.id.btnPlaySpeech);
		btnEditSpeech = (Button)findViewById(R.id.btnEditSpeech);
		btnNewSpeech = (Button)findViewById(R.id.btnNewSpeech);
		
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 1);

		btnPlaySpeech.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SpeechListActivity.class);				
				
				Bundle bun = new Bundle();
				bun.putString("action", "play");
				
				intent.putExtras(bun);
				
				startActivity(intent);

			}
		});
		
		btnEditSpeech.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SpeechListActivity.class);				
				
				Bundle bun = new Bundle();
				bun.putString("action", "edit");
				
				intent.putExtras(bun);
				
				startActivity(intent);
				
			}
		});
		
		btnNewSpeech.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, NewSpeechActivity.class));
				
			}
		});

	}

}
