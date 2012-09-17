package com.mindframe.speechcards;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SpeechListActivity extends Activity {

	ListView lvSpeeches;
	BaseDatosHelper bdh;
	List<Speech> speechList = new ArrayList<Speech>();
	Context context;
	String action;

	public void onCreate(Bundle savedInstanceState) {

		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.speech_list);
		context = this.getApplicationContext();


		Bundle bundle = getIntent().getExtras();
		action = bundle.getString("action");
		
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 1);

		lvSpeeches = (ListView) findViewById(R.id.lvSpeeches);

		speechList = bdh.getSpeechList();
		List<String> titleList = new ArrayList<String>();

		for (Speech speech : speechList) {
			titleList.add(speech.getTitle());
		}

		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titleList);
		
		lvSpeeches.setAdapter(adaptador);
		
		
		lvSpeeches.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				Bundle bun = new Bundle();
				bun.putInt("id_speech", speechList.get(arg2).id_speech);
				bun.putString("speechTitle", speechList.get(arg2).title);
				
				if(action.compareToIgnoreCase("play") == 0){
					Intent intent = new Intent(new Intent(SpeechListActivity.this, SpeechActivity.class));
					intent.putExtras(bun);
					startActivity(intent);
				}else if(action.compareToIgnoreCase("edit") == 0){
					Intent intent = new Intent(new Intent(SpeechListActivity.this, EditSpeechActivity.class));
					intent.putExtras(bun);
					startActivity(intent);
				}
				
			}
		});
		
	}

}
