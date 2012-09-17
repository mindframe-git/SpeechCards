package com.mindframe.speechcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class ChooseActivity extends Activity {
	
	Button btnNewSpeech, btnList;
	final String TAG = getClass().getName();

	public void onCreate(Bundle savedInstanceState) {

		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose);
		
		btnList = (Button)findViewById(R.id.btnList);
		btnNewSpeech = (Button)findViewById(R.id.btnNewSpeech);
		
		
		btnList.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChooseActivity.this, SpeechListActivity.class);				
				
				Bundle bun = new Bundle();
				bun.putString("action", "edit");
				
				intent.putExtras(bun);
				
				startActivity(intent);
				
			}
		});
		
		btnNewSpeech.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ChooseActivity.this, NewSpeechActivity.class));
				
			}
		});
	}
}
