package adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mindframe.speechcards.BaseDatosHelper;
import com.mindframe.speechcards.R;
import com.mindframe.speechcards.model.Card;
import com.mindframe.speechcards.model.Speech;

public class ListAdapter extends ArrayAdapter {

	Context context;
	List<Speech> speechList;
	BaseDatosHelper bdh;
	
	public ListAdapter(Context _context, int resourceId, List<Speech> _speechList) {
		super(_context, resourceId, _speechList);
		this.speechList = _speechList;
		this.context = _context;
		
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.linelist, null);
		
		TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
		TextView tvNCard = (TextView)convertView.findViewById(R.id.tvNCard);
		TextView tvCategory = (TextView)convertView.findViewById(R.id.tvCategory);
		
		bdh = new BaseDatosHelper(context, "SpeechCards", null, 3);
		
		Speech speech = speechList.get(position);
		
		Typeface font = Typeface.createFromAsset(context.getAssets(), "FONT.TTF");
		
		tvTitle.setTypeface(font);
		tvNCard.setTypeface(font);
		tvCategory.setTypeface(font);
		
		tvTitle.setText(speech.getTitle());
		String cat = bdh.getCategoryById(speech.getId_category()).getName();
		if(cat == null || cat.trim().compareToIgnoreCase("") == 0)	{
			tvCategory.setText(R.string.tvNoCategory);
		}else{
			tvCategory.setText(cat);
		}
		
		List<Card> cardList = bdh.getCardsByIdSpeech(speech.getId_speech());
		if(cardList.isEmpty()){
			tvNCard.setText(R.string.toastNoCards);
		}else if(cardList.size() == 1){
			tvNCard.setText("1 " + context.getString(R.string.card) + ".");
		}else{
			tvNCard.setText(cardList.size() + context.getString(R.string.card) +"s.");
		}
		
		return convertView;
	}
	
}
