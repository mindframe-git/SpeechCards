package com.mindframe.speechcards;

import java.util.ArrayList;
import java.util.List;

import com.mindframe.speechcards.adapter.ListAdapter;
import com.mindframe.speechcards.model.Speech;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class SpeechListActivity extends Activity {

	ListView lvSpeeches;
	TextView tvTitleList;
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
		tvTitleList = (TextView) findViewById(R.id.tvTitleList);

		tvTitleList.setTypeface(Typeface.createFromAsset(getAssets(), "FONT.TTF"));

		Bundle bundle = getIntent().getExtras();
		action = bundle.getString("action");

		if (action.compareToIgnoreCase("play") == 0) {
			tvTitleList.setText(R.string.tvTitleListPlay);
		} else if (action.compareToIgnoreCase("edit") == 0) {
			tvTitleList.setText(R.string.tvTitleListEdit);
		}

		cargaLista();

		lvSpeeches.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				Bundle bun = new Bundle();
				bun.putInt("id_speech", speechList.get(arg2).getId_speech());
				bun.putString("speechTitle", speechList.get(arg2).getTitle());
				bun.putString("action", action);

				if (action.compareToIgnoreCase("play") == 0) {
					tvTitleList.setText(R.string.tvTitleListPlay);
					Intent intent = new Intent(new Intent(SpeechListActivity.this, SpeechActivity.class));
					intent.putExtras(bun);
					startActivity(intent);
					finish();
				} else if (action.compareToIgnoreCase("edit") == 0) {
					tvTitleList.setText(R.string.tvTitleListEdit);
					Intent intent = new Intent(new Intent(SpeechListActivity.this, ManageSpeechActivity.class));
					intent.putExtras(bun);
					startActivity(intent);
					finish();
				}

			}
		});

		registerForContextMenu(lvSpeeches);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();

		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		Log.d(TAG, "Posicion: " + info.position);
		menu.setHeaderTitle("Opciones");

		inflater.inflate(R.menu.opt_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		switch (item.getItemId()) {
		case R.id.optEditSpeech:
			Bundle bun = new Bundle();
			bun.putString("action", "edit");
			bun.putInt("id_speech", speechList.get(info.position).getId_speech());
			
			Intent intent = new Intent(SpeechListActivity.this, NewSpeechActivity.class);
			
			intent.putExtras(bun);
			
			startActivity(intent);
			
			return true;
		
		case R.id.optDelSpeech:
			Log.d(TAG, "Posicion2: " + info.position);
			crearDialogoConfirmacion(info.position);
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}

	private Dialog crearDialogoConfirmacion(final int position) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialTitle);
		builder.setMessage(R.string.dialMessageSpeech);
		builder.setPositiveButton(R.string.dialAcept, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				bdh.deleteSpeech(speechList.get(position).getId_speech());
				Toast.makeText(context, R.string.toastDelSpeech, Toast.LENGTH_SHORT).show();
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
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume()");
		cargaLista();
	}

	public void cargaLista() {
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 3);

		speechList = bdh.getSpeechList();

		if (speechList.isEmpty()) {
			Toast.makeText(context, R.string.toastNoSpeech, Toast.LENGTH_SHORT).show();
			finish();
		}

		ListAdapter adaptador = new ListAdapter(context, R.layout.speech_line, speechList);

		lvSpeeches.setAdapter(adaptador);
	}
}
