package com.mindframe.speechcards;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
/**
 * 
 * Crea la base de datos y realiza 
 * todas las operaciones entre la bdd y la app
 * 
 * 
 * @author mindframe
 *
 */
public class BaseDatosHelper extends SQLiteOpenHelper {

	String SQL_CREATE_SPEECH = "create table speech (id_speech integer, title text)";
	String SQL_CREATE_CARD = "create table card (id_card integer, id_speech integer, id_prev_card integer, id_next_card intenger, header text, body text)";

	public static final String TABLE_NAME_SPEECH = "SPEECH";
	public static final String TABLE_NAME_CARD = "CARD";

	public static class speechColums implements BaseColumns {
		public static final String TITLE = "TITLE";
		public static final String SIZE = "SIZE";
		public static final String COLOR = "COLOR";
		
	}

	public static class cardColumns implements BaseColumns {
		public static final String ID_SPEECH = "ID_SPEECH";
		public static final String ID_PREV_CARD = "ID_PREV_CARD";
		public static final String ID_NEXT_CARD = "ID_NEXT_CARD";
		public static final String HEADER = "HEADER";
		public static final String BODY = "BODY";
	}

	public BaseDatosHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
		// db.execSQL(SQL_CREATE_CARD);dddd
		// db.execSQL(SQL_CREATE_SPEECH);
	}
	
	public void createTables(SQLiteDatabase db){
		
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();

		sb1.append("CREATE TABLE " + TABLE_NAME_SPEECH + " (");
		sb1.append(BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
		sb1.append(speechColums.TITLE + " TEXT NOT NULL, ");
		sb1.append(speechColums.SIZE + " INTEGER, ");
		sb1.append(speechColums.COLOR + " TEXT");
		sb1.append(");");

		sb2.append("CREATE TABLE " + TABLE_NAME_CARD + " (");
		sb2.append(BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
		sb2.append(cardColumns.ID_SPEECH + " INTEGER NOT NULL, ");
		sb2.append(cardColumns.ID_PREV_CARD + " INTEGER, ");
		sb2.append(cardColumns.ID_NEXT_CARD + " INTEGER, ");
		sb2.append(cardColumns.HEADER + " TEXT, ");
		sb2.append(cardColumns.BODY + " TEXT, ");
		sb2.append("FOREIGN KEY (" + cardColumns.ID_SPEECH + ") REFERENCES " + TABLE_NAME_SPEECH+"("+BaseColumns._ID+") ");
		sb2.append(");");

		db.execSQL(sb1.toString());
		db.execSQL(sb2.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("ALTER TABLE SPEECH ADD SIZE TEXT");
		db.execSQL("ALTER TABLE SPEECH ADD COLOR TEXT");
		
//		db.execSQL("DROP TABLE IF EXISTS card");
//		db.execSQL("DROP TABLE IF EXISTS speech");
//
//		createTables(db);
	}

	public int newSpeech(String title, int size) {
		
		int id_speech;
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(speechColums.TITLE, title.trim());
		cv.put(speechColums.SIZE, size);
		
		id_speech = (int) db.insert(TABLE_NAME_SPEECH, null, cv);
		db.close();
		
		return id_speech;
	}

	public boolean existsSpeech(String title) {
		boolean exists = false;
		
		String sql = "SELECT * FROM " + TABLE_NAME_SPEECH + " WHERE " + speechColums.TITLE + " = '" + title.trim() + "' COLLATE NOCASE";
		SQLiteDatabase db = this.getReadableDatabase();
		
		
		Cursor c = db.rawQuery(sql, null);
		
		if(c.getCount() > 0){
			exists = true;
		}
		db.close();
		
		
		return exists;
	}
	
	public List<Speech> getSpeechList(){
		
		List<Speech> speechList = new ArrayList<Speech>();
		
		String[] columns = {speechColums._ID, speechColums.TITLE};
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor c = db.query(TABLE_NAME_SPEECH, columns, null, null, null, null, null, null);
		
		if(c.moveToFirst()){
			do{
				Speech speech = new Speech();
				speech.id_speech = c.getInt(0);
				speech.title = c.getString(1);
				
				speechList.add(speech);
				
			}while(c.moveToNext());
		}
		
		db.close();
		return speechList;
	}
	/**
	 * 
	 * Si actCard es true, tenemos que actualizar la tarjeta anterior(id_prev_card) poniendole en 
	 * id_next_card el id de esta nueva tarjeta, que nos lo devolverá al insertarla.
	 * 
	 * @param card
	 * @param actCard
	 * @return idNewCard
	 */
	public int addCard(Card card, boolean actCard) {
		
		int idNewCard;
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put(cardColumns.BODY, card.body);
		values.put(cardColumns.HEADER, card.header);
		values.put(cardColumns.ID_SPEECH, card.id_speech);
		values.put(cardColumns.ID_PREV_CARD, card.id_prev_card);
		values.put(cardColumns.ID_NEXT_CARD, card.id_next_card);
		
		
		idNewCard = (int) db.insert(TABLE_NAME_CARD, null, values);
		
		if(actCard){
			updatePrev(card.id_speech, card.id_prev_card, idNewCard);
		}
		//Es una tarjeta de enmedio, tenemos que actualizar el id_prev de la siguiente.
		if(card.id_next_card != 0){
			updateNext(card.id_speech, card.id_next_card, idNewCard);
		}
			
		db.close();
		return idNewCard;
	}
	
	
	

	/**
	 * Cuando agreguemos una tarjeta nueva, tenemos 
	 * que actualizar el id_next_card de la anterior
	 * 
	 * 
	 * @param id_speech
	 * @param id_prev_card
	 * @param idNewCard
	 */
	private void updatePrev(int id_speech, int id_prev_card, int idNewCard) {
	
		ContentValues values = new ContentValues();
		
		values.put(cardColumns.ID_NEXT_CARD, idNewCard);
		SQLiteDatabase db = this.getReadableDatabase();
		
		String where = cardColumns.ID_SPEECH + "= " + id_speech + " and " + cardColumns._ID + " = " + id_prev_card ; 
		
		db.update(TABLE_NAME_CARD, values, where, null);
		
		db.close();
		
	}
	
	/**
	 * Cuando añadamos una tarjeta nueva al final 
	 * tenemos que modificar el id_prev de la siguiente.
	 * 
	 * @param id_speech
	 * @param id_prev_card
	 * @param idNewCard
	 */
	private void updateNext(int id_speech, int id_next_card, int idNewCard) {
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(cardColumns.ID_PREV_CARD, idNewCard);
		
		String where = cardColumns.ID_SPEECH + "= " + id_speech + " and " + cardColumns._ID + " = " + id_next_card ; 
		
		db.update(TABLE_NAME_CARD, values, where, null);
		
		db.close();
	}
	
	public List<Card> getCardsByIdSpeech(int id_speech){
		List<Card> cardList = new ArrayList<Card>();
		
		String[] columns = {cardColumns._ID, cardColumns.ID_SPEECH, cardColumns.ID_PREV_CARD, 
							cardColumns.ID_NEXT_CARD, cardColumns.HEADER, cardColumns.BODY};
		String[] args = {String.valueOf(id_speech)};
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor c = db.query(TABLE_NAME_CARD, columns, "id_speech=?", args, null, null, null, null);
		
		if(c.moveToFirst()){
			do{
				Card card = new Card();
				
				card.id_card = c.getInt(0);
				card.id_speech = c.getInt(1);
				card.id_prev_card = c.getInt(2);
				card.id_next_card = c.getInt(3);
				card.header = c.getString(4);
				card.body = c.getString(5);
				
				cardList.add(card);
			}while(c.moveToNext());
		}
		
		db.close();
		return cardList;
		
	}
	
	public Speech getSpeechById(int id_speech){
		Speech speech = new Speech();
		
		String[] columns = {speechColums.TITLE, speechColums.SIZE, speechColums.COLOR, speechColums._ID};
		String[] args = {String.valueOf(id_speech)};
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor c = db.query(TABLE_NAME_SPEECH, columns, "_id=?", args, null, null, null, null);
		if(c.moveToFirst()){
			speech.title=c.getString(0);
			speech.size = c.getInt(1);
			speech.color = c.getString(2);
			speech.id_speech = c.getInt(3);
			
		}while(c.moveToNext());

		
		db.close();
		return speech;
	}
	
	public Card getCardById(int id_card){
		Card card = new Card();
		
		String[] columns = {cardColumns._ID, cardColumns.ID_SPEECH, cardColumns.BODY, cardColumns.HEADER, cardColumns.ID_NEXT_CARD, cardColumns.ID_PREV_CARD};
		String[] args = {String.valueOf(id_card)};
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor c = db.query(TABLE_NAME_CARD, columns, "_id=?", args, null, null, null, null);
		
		if(c.moveToFirst()){
			card.id_card =c.getInt(0);
			card.id_speech =c.getInt(1);
			card.body =c.getString(2);
			card.header =c.getString(3);
			card.id_next_card =c.getInt(4);
			card.id_prev_card =c.getInt(5);
			
		}while(c.moveToNext());
		
		db.close();
	
		return card;
	}

	/**
	 * Aquí recibiré una carta a la cual se le ha 
	 * modificado la cabecera o el texto. UPDATE: 
	 * @param card
	 * @param Header 
	 * @param Body 
	 */
	public boolean updateCard(Card card, String header, String body) {
		int result = 0;
		ContentValues val = new ContentValues();
		val.put(cardColumns.HEADER, header);
		val.put(cardColumns.BODY, body);
		String whereClause =cardColumns._ID + " = " + card.id_card;
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		result = db.update(TABLE_NAME_CARD, val, whereClause, null);
		
		db.close();
		
		if(result != 1)
			return false;
		else 
			return true;
	}
	
	public void updateSpeech(Speech speech){
		ContentValues val = new ContentValues();
		val.put(speechColums.TITLE, speech.title);
		val.put(speechColums.SIZE, speech.size);
		val.put(speechColums.COLOR, speech.color);
		String whereClause = speechColums._ID + " =" + speech.id_speech;
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		db.update(TABLE_NAME_SPEECH, val, whereClause, null);
		
		db.close();
	}

	
	/**
	 * Borramos la tarjeta que recibimos y actualizamos la ordenación
	 * de la lista. Se dan tres casos.
	 * 1º Es la primera tarjeta:
	 * 	Acualizamos el id_prev_card = 0 de la siguiente (si tiene) y borramos
	 * 2º Es la última:
	 * 	Actualizamos id_next_card = 0 de la anterior (si lo tiene) y borramos
	 * 3º Está en medio.
	 * 	todo lo anterior
	 * @param card
	 */
	public int delCard(Card card) {
		
		
		SQLiteDatabase db = this.getReadableDatabase();
		if(card.isFirst()){
			ContentValues cv = new ContentValues();
			cv.put(cardColumns.ID_PREV_CARD, 0);
			String whereClause = cardColumns._ID + " = " + card.id_next_card;
			db.update(TABLE_NAME_CARD, cv, whereClause, null);
		}if(card.isLast()){
			ContentValues cv = new ContentValues();
			cv.put(cardColumns.ID_NEXT_CARD, 0);
			String whereClause = cardColumns._ID + " = " + card.id_prev_card;
			db.update(TABLE_NAME_CARD, cv, whereClause, null);
		}if(!card.isFirst() && !card.isLast()){
			//Cambiamos la tarjeta previa
			ContentValues cv_prev = new ContentValues();
			cv_prev.put(cardColumns.ID_NEXT_CARD, card.id_next_card);
			String whereClause_prev = cardColumns._ID + " = " + card.id_prev_card;
			db.update(TABLE_NAME_CARD, cv_prev, whereClause_prev, null);
			
			//cambiamos la tarjeta siguiente
			ContentValues cv_next = new ContentValues();
			cv_next.put(cardColumns.ID_PREV_CARD, card.id_prev_card);
			String whereClause_next = cardColumns._ID + " = " + card.id_next_card;
			db.update(TABLE_NAME_CARD, cv_next, whereClause_next, null);
		}
		
		//Borramos la tarjeta.
		int result = db.delete(TABLE_NAME_CARD, cardColumns._ID + " = " + card.id_card, null);
		
		db.close();
		
		return result;
	}
	
	
	/**
	 * 1º Se borran las tarjetas asociadas al discurso
	 * 2º Se borra el discurso
	 * 
	 * @param id_speech
	 */
	public void deleteSpeech(int id_speech) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String whereClauseCard = cardColumns.ID_SPEECH +  " = " + id_speech; 
		String whereClauseSpeech = speechColums._ID + " = " + id_speech;
		
		db.delete(TABLE_NAME_CARD, whereClauseCard, null);
		db.delete(TABLE_NAME_SPEECH, whereClauseSpeech, null);
		
		db.close();
	}
	
	
	
}