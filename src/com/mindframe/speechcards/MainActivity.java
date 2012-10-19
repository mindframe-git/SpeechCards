package com.mindframe.speechcards;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

//	ImageView btnEditSpeech, btnPlaySpeech, btnNewSpeech;
	TextView tvTitleApp, tvNew, tvEdit, tvPlay;
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

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);

		context = this.getApplicationContext();

		tvTitleApp = (TextView)findViewById(R.id.tvTitleApp);
		tvNew = (TextView)findViewById(R.id.tvNew);
		tvEdit = (TextView)findViewById(R.id.tvEdit);
		tvPlay = (TextView)findViewById(R.id.tvPlay);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "FONT.TTF");
		tvTitleApp.setTypeface(font);
		tvNew.setTypeface(font);
		tvEdit.setTypeface(font);
		tvPlay.setTypeface(font);
		
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 2);

		
		tvPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SpeechListActivity.class);				
				
				Bundle bun = new Bundle();
				bun.putString("action", "play");
				
				intent.putExtras(bun);
				
				startActivity(intent);

			}
		});
		
		tvEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SpeechListActivity.class);				
				
				Bundle bun = new Bundle();
				bun.putString("action", "edit");
				
				intent.putExtras(bun);
				
				startActivity(intent);
				
			}
		});
		
		tvNew.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, NewSpeechActivity.class));
				
			}
		});

	}
}