package com.mindframe.speechcards;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class SpeechListActivity extends Activity {

	ListView lvSpeeches;
	BaseDatosHelper bdh;
	List<Speech> speechList = new ArrayList<Speech>();
	Context context;
	String action;
	
	final String TAG = getClass().getName();

	public void onCreate(Bundle savedInstanceState) {

		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.speech_list);
		context = this.getApplicationContext();
		
		lvSpeeches = (ListView) findViewById(R.id.lvSpeeches);

		Bundle bundle = getIntent().getExtras();
		action = bundle.getString("action");
		
		cargaLista();
		
		
		lvSpeeches.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				Bundle bun = new Bundle();
				bun.putInt("id_speech", speechList.get(arg2).id_speech);
				bun.putString("speechTitle", speechList.get(arg2).title);
				bun.putString("action", action);
				
				if(action.compareToIgnoreCase("play") == 0){
					Intent intent = new Intent(new Intent(SpeechListActivity.this, SpeechActivity.class));
					intent.putExtras(bun);
					startActivity(intent);
				}else if(action.compareToIgnoreCase("edit") == 0){
					Intent intent = new Intent(new Intent(SpeechListActivity.this, ManageSpeechActivity.class));
					intent.putExtras(bun);
					startActivity(intent);
				}
				
			}
		});
		
		lvSpeeches.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				crearDialogoConfirmacion(arg2);
				
				return false;
			}
		});
		
	}
	
	
	private Dialog crearDialogoConfirmacion(final int position) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialTitle);
		builder.setMessage(R.string.dialMessage);
		builder.setPositiveButton(R.string.dialAcept, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				bdh.deleteSpeech(speechList.get(position).id_speech);
				Toast.makeText(context, R.string.toastDelCard, Toast.LENGTH_SHORT).show();
				cargaLista();
				dialog.cancel();
			}
		});
		builder.setNegativeButton(R.string.dialCancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		return builder.show();
	}
	
	
	@Override
	public void onResume(){
		super.onResume();
		Log.d(TAG, "onResume()");
		cargaLista();
	}
	
	public void cargaLista(){
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 2);

		speechList = bdh.getSpeechList();
		
		if(speechList.isEmpty()){
			Toast.makeText(context, R.string.toastNoSpeech, Toast.LENGTH_SHORT).show();
			finish();
		}
		
		ListAdapter adaptador = new ListAdapter(context, R.layout.linelist, speechList);
		
		lvSpeeches.setAdapter(adaptador);
	}
}
