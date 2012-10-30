package com.mindframe.speechcards;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mindframe.speechcards.model.Category;

public class NewCategoryActivity extends Activity{
	
	TextView tvTitle, tvNew, tvRed, tvGreen, tvBlue, tvBarraColor, btnNewCategory;
	SeekBar sbRed, sbGreen, sbBlue;
	EditText etNewCategory;
	ImageView btnBack;
	int alfa = 127;
	int red, green, blue;
	
	BaseDatosHelper bdh;
	Context context;
	final String TAG = getClass().getName();

	public void onCreate(Bundle savedInstanceState) {

		getWindowManager().getDefaultDisplay().getHeight();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_category);
		
		context = this.getApplicationContext();
		
		tvTitle = (TextView)findViewById(R.id.tvTitle);
		tvNew = (TextView)findViewById(R.id.tvNew);
		tvRed = (TextView)findViewById(R.id.tvRed);
		tvGreen = (TextView)findViewById(R.id.tvGreen);
		tvBlue = (TextView)findViewById(R.id.tvBlue);
		tvBarraColor = (TextView)findViewById(R.id.tvBarraColor);
		btnNewCategory = (TextView)findViewById(R.id.btnNewCategory);
		sbRed = (SeekBar)findViewById(R.id.sbRed);
		sbGreen = (SeekBar)findViewById(R.id.sbGreen);
		sbBlue = (SeekBar)findViewById(R.id.sbBlue);
		btnBack = (ImageView)findViewById(R.id.btnBack);
		etNewCategory = (EditText)findViewById(R.id.etNewCategory);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "FONT.TTF");
		
		tvTitle.setTypeface(font);
		tvNew.setTypeface(font);
		tvRed.setTypeface(font);
		tvGreen.setTypeface(font);
		tvBlue.setTypeface(font);
		btnNewCategory.setTypeface(font);
		
		
		sbRed.setProgress(127);
		sbGreen.setProgress(127);
		sbBlue.setProgress(127);
		
		changeColor();

		sbRed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				red = progress;
				changeColor();

			}
		});

		sbGreen.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				green = progress;
				changeColor();

			}
		});

		sbBlue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				blue = progress;
				changeColor();

			}
		});
		
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(etNewCategory.getText().toString().trim().compareToIgnoreCase("") == 0){
					finish();
				}else{
					newCategory();
				}
				
			}
		});
		
		btnNewCategory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				newCategory();
				
			}
		});
		

	}

	private void changeColor() {

		tvBarraColor.setBackgroundColor(Color.argb(alfa, red, green, blue));

	}
	
	private void newCategory(){
		
		Category cat = new Category();
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 3);
		
		String name = etNewCategory.getText().toString();
		String stColor = String.valueOf(alfa) + "," + String.valueOf(red) + "," + String.valueOf(green) + "," + String.valueOf(blue);
		
		cat.setName(name);
		cat.setColor(stColor);
		
		if(cat.getName().compareToIgnoreCase("") != 0){
			if(!bdh.existsCategory(cat.getName())){
				bdh.newCategory(cat);
				
				finish();
			}else{
				Toast.makeText(context, R.string.toastExistsCategory, Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(context, R.string.toastVoidName, Toast.LENGTH_SHORT).show();
		}
		
	}
}