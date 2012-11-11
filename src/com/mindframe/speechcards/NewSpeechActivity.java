package com.mindframe.speechcards;

import java.util.ArrayList;
import java.util.List;

import com.mindframe.speechcards.model.Category;
import com.mindframe.speechcards.model.Speech;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
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
	TextView tvTitle, tvNew, btnNewSpeech, tvCategoryBar;
	ImageView btnBack;

	Context context;
	BaseDatosHelper bdh;
	List<Category> catList = new ArrayList<Category>();
	String action;
	int id_speech;
	
	Category currentCat = new Category();
	final String TAG = getClass().getName();
	boolean grabado;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		context = this.getApplicationContext();
		setContentView(R.layout.new_speech);

		bdh = new BaseDatosHelper(context, "SpeechCards", null, 3);
		btnNewSpeech = (TextView) findViewById(R.id.btnNewSpeech);
		etNewSpeech = (EditText) findViewById(R.id.etNewSpeech);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvNew = (TextView) findViewById(R.id.tvNew);
		tvCategoryBar = (TextView) findViewById(R.id.tvCategoryBar);
		btnBack = (ImageView)findViewById(R.id.btnBack);


		Typeface font = Typeface.createFromAsset(getAssets(), "FONT.TTF");
		tvTitle.setTypeface(font);
		tvNew.setTypeface(font);
		btnNewSpeech.setTypeface(font);
		etNewSpeech.setTypeface(font);
		tvCategoryBar.setTypeface(font);

		Bundle bundle = getIntent().getExtras();
		action = bundle.getString("action");
		id_speech = bundle.getInt("id_speech");

		tvCategoryBar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(NewSpeechActivity.this, CategoryListActivity.class), 1);

			}
		});

		if (action.compareToIgnoreCase("new") == 0) {
			actionNew();
		} else if (action.compareToIgnoreCase("edit") == 0) {
			actionEdit();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case (1): 
			if (resultCode == Activity.RESULT_OK) {
				String valorDevuelto = data.getStringExtra("id_category");
				Log.d(TAG, "valor devuelto: " + valorDevuelto);
				currentCat = bdh.getCategoryById(Integer.valueOf(valorDevuelto));
				
				pintaCategoria();
			}
			break;
		
		}
	}

	private void actionEdit() {

		tvTitle.setText(R.string.tvEditCollecion);
		btnNewSpeech.setText(R.string.btnMod);
		final Speech speech = bdh.getSpeechById(id_speech);
		etNewSpeech.setText(speech.getTitle());
		currentCat = bdh.getCategoryById(speech.getId_category());
		pintaCategoria();
		
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (etNewSpeech.getText().toString().trim().compareToIgnoreCase("") == 0) {
					Toast.makeText(context, R.string.toastVoidName, Toast.LENGTH_SHORT).show();
				} else {
					speech.setTitle(etNewSpeech.getText().toString());
					speech.setId_category(currentCat.getId());

					bdh.updateSpeech(speech);

					finish();
				}
			}
		});

		btnNewSpeech.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (etNewSpeech.getText().toString().trim().compareToIgnoreCase("") == 0) {
					Toast.makeText(context, R.string.toastVoidName, Toast.LENGTH_SHORT).show();
				} else {
					speech.setTitle(etNewSpeech.getText().toString());
					speech.setId_category(currentCat.getId());

					bdh.updateSpeech(speech);

					finish();
				}
			}

		});

	}


	private void actionNew() {
		
		btnBack.setVisibility(View.GONE);
		
		// Al pulsar enter, se crea la colección
		grabado = false;
		etNewSpeech.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					if (!grabado) {
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

		pintaCategoria();

	}
	
	public void pintaCategoria(){
		if(currentCat.getName() != null){
			tvCategoryBar.setText(currentCat.getName());
			tvCategoryBar.setBackgroundColor(currentCat.getColorCode());
		}else{
			tvCategoryBar.setText(R.string.tvNoCategory);
			tvCategoryBar.setBackgroundColor(Color.argb(127,127,127,127));
		}
		
	}

	public void nuevaColeccion() {

		String speechTitle = etNewSpeech.getText().toString();
		if (speechTitle == null || speechTitle.trim().compareToIgnoreCase("") == 0) {
			Toast.makeText(context, R.string.toastVoidName, Toast.LENGTH_SHORT).show();
		} else {
			if (!bdh.existsSpeech(speechTitle)) {

				Bundle bun = new Bundle();

				Speech speech = new Speech();
				//TODO: Esto y más valores habría que meterlos en ficheros de propiedades
				speech.setSize(20);
				speech.setTitle(speechTitle);
				if(currentCat != null)
					speech.setId_category(currentCat.getId());

				speech.setId_speech(bdh.newSpeech(speech));

				Intent intent = new Intent(NewSpeechActivity.this, ManageSpeechActivity.class);
				bun.putString("speechTitle", speech.getTitle());
				bun.putInt("id_speech", speech.getId_speech());
				bun.putInt("id_prev_card", -1);

				intent.putExtras(bun);

				startActivity(intent);
				finish();

			} else {
				Toast.makeText(context, R.string.toastExistsSpeech, Toast.LENGTH_SHORT).show();
			}
		}
	}
}
