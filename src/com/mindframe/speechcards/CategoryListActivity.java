package com.mindframe.speechcards;

import java.util.ArrayList;
import java.util.List;

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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindframe.speechcards.adapter.CategoryAdapter;
import com.mindframe.speechcards.model.Category;

public class CategoryListActivity extends Activity{
	
	TextView tvTitleList, btnNewCategory;
	ListView lvCat;
	
	List<Category> catList = new ArrayList<Category>();
	BaseDatosHelper bdh;
	Context context;
	final String TAG = getClass().getName();

	public void onCreate(Bundle savedInstanceState) {

		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_list);
		context = this.getApplicationContext();
		
		
		tvTitleList = (TextView)findViewById(R.id.tvTitleList);
		btnNewCategory = (TextView)findViewById(R.id.btnNewCategory);
		lvCat = (ListView)findViewById(R.id.lvCat);
		
		tvTitleList.setTypeface(Typeface.createFromAsset(getAssets(), "FONT.TTF"));
		btnNewCategory.setTypeface(Typeface.createFromAsset(getAssets(), "FONT.TTF"));
		cargaLista();
		
		lvCat.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent resultIntent;

				resultIntent = new Intent();
				resultIntent.putExtra("id_category", String.valueOf(catList.get(arg2).getId()));
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
				
			}
		});
		
		btnNewCategory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bun = new Bundle();
				bun.putString("action", "new");
				Intent intent = new Intent(CategoryListActivity.this, NewCategoryActivity.class);
				intent.putExtras(bun);
				
				
				startActivity(intent);
			}
		});
		
		registerForContextMenu(lvCat);
		
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
			bun.putInt("id_cat", catList.get(info.position).getId());
			
			Intent intent = new Intent(CategoryListActivity.this, NewCategoryActivity.class);
			
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
				bdh.deleteCategory(catList.get(position));
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
	
	public void cargaLista(){
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 3);
		
		catList = bdh.getCatList();
		
		CategoryAdapter adapter = new CategoryAdapter(context, R.layout.category_line, catList);
		
		lvCat.setAdapter(adapter);
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		cargaLista();
	}

}
