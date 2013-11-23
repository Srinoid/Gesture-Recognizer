package com.dev.nativegesturerecognizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.sql.Blob;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DBAdapter extends SQLiteOpenHelper{

	
	
	public static final String KEY_ID = "id";
	public static final String KEY_DATA ="data";
	public static final String KEY_CB = "codebook";
	public static final String KEY_HMM = "hmm";
	private static final String TAG = "dbadapter";

	static final String DATABASE_NAME = "3d_gestures";
	private static final String DATABASE_TABLE = "gestures";
	private static final int DATABASE_VERSION = 1;
    private static String DB_PATH = "/data/data/com.dev.nativegesturerecognizer/databases/";
	public Cursor mcursor;
	SQLiteDatabase gesturedb =  null;
	private Context context;
	
	DBAdapter(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}
		
	public void createDataBase() throws IOException{
		 
		boolean dbExist = checkDataBase();
		 
		if(dbExist){
		//do nothing - database already exist
		}else{
		 
		//By calling this method and empty database will be created into the default system path
		//of your application so we are gonna be able to overwrite that database with our database.
		this.getWritableDatabase();
		 
		try {
		this.close() ;
		copyDataBase();
		 
		} catch (IOException e) {
		 
		throw new Error("Error copying database");
		 
		}
		}
		 
	}	
	
	private boolean checkDataBase(){
			 
			SQLiteDatabase checkDB = null;
			 
			try{
			String myPath = DB_PATH + DATABASE_NAME;
			gesturedb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
			 
			}catch(SQLiteException e){
			 
			//database does't exist yet.
			 
			}
			 
			if(gesturedb != null){
			 
			gesturedb.close();
			 
			}
			 
			return gesturedb != null ? true : false;
			}
			 
			/**
			  * Copies your database from your local assets-folder to the just created empty database in the
			  * system folder, from where it can be accessed and handled.
			  * This is done by transfering bytestream.
			  * */
			private void copyDataBase() throws IOException{
			 
			//Open your local db as the input stream
				File f = new File(DB_PATH);
				if (!f.exists()) {
				f.mkdir();
				}	
			InputStream myInput = context.getAssets().open(DATABASE_NAME);
			 
			// Path to the just created empty db
			String outFileName = DB_PATH + DATABASE_NAME;
			File fo = new File(DB_PATH + DATABASE_NAME);
			fo.createNewFile();
			//Open the empty db as the output stream
			OutputStream myOutput = new FileOutputStream(outFileName);
			 
			//transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
			}
			 
			//Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();
			 
			}
			 
			public void openDataBase() throws SQLException{
			 
			//Open the database
			String myPath = DB_PATH + DATABASE_NAME;
			gesturedb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
			 
			}
			 
			@Override
			public synchronized void close() {
			 
			if(gesturedb != null)
			gesturedb.close();
			 
			super.close();
			 
			}
			 
			@Override
			public void onCreate(SQLiteDatabase db) {
			 
			}
			 
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			 
			}
			
			
			
			public Cursor query(){
				
				Cursor c = gesturedb.rawQuery("SELECT * FROM gestures", null);
				
				return c;
				
			}
			 
			// Add your public helper methods to access and get content from the database.
			// You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
			// to you to create adapters for your views.
			 
			}
	
	