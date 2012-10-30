package com.mindframe.speechcards;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mindframe.speechcards.model.Category;
import com.mindframe.speechcards.model.Speech;

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
	TextView tvTitle, tvNew, btnNewSpeech, tvCategory;
	Spinner spCategory;
	Button btnAddCat;

	Context context;
	int textSize;
	BaseDatosHelper bdh;
	List<Category> catList = new ArrayList<Category>();
	String action;
	int id_speech;

	final String TAG = getClass().getName();
	boolean grabado;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		context = this.getApplicationContext();
		setContentView(R.layout.new_speech);

		// El tamaño de texto predeterminado es 20
		textSize = 20;

		bdh = new BaseDatosHelper(context, "SpeechCards", null, 3);
		btnNewSpeech = (TextView) findViewById(R.id.btnNewSpeech);
		etNewSpeech = (EditText) findViewById(R.id.etNewSpeech);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvNew = (TextView) findViewById(R.id.tvNew);
		tvCategory = (TextView) findViewById(R.id.tvCategoria);
		spCategory = (Spinner) findViewById(R.id.spCategory);
		btnAddCat = (Button) findViewById(R.id.btnAddCat);

		Typeface font = Typeface.createFromAsset(getAssets(), "FONT.TTF");
		tvTitle.setTypeface(font);
		tvNew.setTypeface(font);
		btnNewSpeech.setTypeface(font);
		etNewSpeech.setTypeface(font);
		tvCategory.setTypeface(font);

		Bundle bundle = getIntent().getExtras();
		action = bundle.getString("action");
		id_speech = bundle.getInt("id_speech");

		loadSpinner();

		if (action.compareToIgnoreCase("new") == 0) {
			actionNew();
		} else if (action.compareToIgnoreCase("edit") == 0) {
			actionEdit();
		}

		btnAddCat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(NewSpeechActivity.this, NewCategoryActivity.class));
				// addCategory();

			}
		});

	}

	private void actionEdit() {

		tvTitle.setText("Editar Colección");
		btnNewSpeech.setText("Guardar");
		final Speech speech = bdh.getSpeechById(id_speech);
		etNewSpeech.setText(speech.getTitle());

		spCategory.setSelection(getCatPosition(speech.getId_category()));

		btnNewSpeech.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (etNewSpeech.getText().toString().trim().compareToIgnoreCase("") == 0) {
					Toast.makeText(context, R.string.toastVoidName, Toast.LENGTH_SHORT).show();
				} else {
					speech.setTitle(etNewSpeech.getText().toString());
					speech.setId_category(catList.get(spCategory.getSelectedItemPosition()).getId());

					bdh.updateSpeech(speech);

					finish();
				}
			}

		});

	}

	private int getCatPosition(int id_category) {

		for (int i = 1; i < catList.size(); i++) {
			if (catList.get(i).getId() == id_category)
				return i;
		}

		return 0;

	}

	private void actionNew() {

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

		loadSpinner();

	}

	// private void addCategory() {
	//
	// AlertDialog.Builder newCatBuilder = new AlertDialog.Builder(this);
	// newCatBuilder.setTitle(R.string.dialTitleNewCat);
	// final EditText catName = new EditText(this);
	// final SeekBar sb = new SeekBar(this);
	// catName.setSingleLine();
	//
	// newCatBuilder.setView(catName);
	//
	// newCatBuilder.setPositiveButton(R.string.dialAcept, new
	// DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int which) {
	// Category cat = new Category();
	// cat.setName(catName.getText().toString().trim());
	// if(cat.getName().compareToIgnoreCase("") != 0){
	// if(!bdh.existsCategory(cat.getName())){
	// bdh.newCategory(cat);
	//
	// loadSpinner();
	// }else{
	// Log.d(TAG, "Ya existe categoría con ese nombre, bitch");
	// }
	// }else{
	// Log.d(TAG, "Debe escribir un nombre, mardito!");
	// }
	// }
	// });
	// newCatBuilder.setNegativeButton(R.string.dialCancel, new
	// DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	//
	// }
	// });
	// AlertDialog newCatDialog = newCatBuilder.create();
	// newCatDialog.show();
	// }

	public void loadSpinner() {

		catList = bdh.getCatList();

		// Esto que voy a hacer es una barrabasada y en la siguiente versión lo
		// tengo que quitar!
		if (catList.isEmpty()) {
			Category firstCat = new Category(getText(R.string.defaultCat).toString(), "127,255,255,255");
			bdh.newCategory(firstCat);
			catList = bdh.getCatList();
		}

		List<String> catNames = new ArrayList<String>();

		for (Category cat : catList) {
			catNames.add(cat.getName());
		}
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catNames);

		adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spCategory.setAdapter(adaptador);

	}

	public void nuevaColeccion() {

		String speechTitle = etNewSpeech.getText().toString();
		if (speechTitle == null || speechTitle.trim().compareToIgnoreCase("") == 0) {
			Toast.makeText(context, R.string.toastVoidName, Toast.LENGTH_SHORT).show();
		} else {
			if (!bdh.existsSpeech(speechTitle)) {

				Bundle bun = new Bundle();

				Speech speech = new Speech();
				speech.setSize(textSize);
				speech.setTitle(speechTitle);
				speech.setId_category(catList.get(spCategory.getSelectedItemPosition()).getId());

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

	@Override
	public void onResume() {
		super.onResume();
		if (action != null && action.compareToIgnoreCase("edit") != 0)
			loadSpinner();
	}

}
