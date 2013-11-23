package com.dev.nativegesturerecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import com.example.dtwgesturerecognizer.Point;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;



public class GestureChecker {
  
	
    String detGesture = "";
	Vector<Vector<Vector<Point>>> dt;
    Vector<String> name;
	
    static{
		System.loadLibrary("dtw");
	       }
    
    
    
	
	GestureChecker(Context a) {
		name = new Vector<String>();
		dt = new Vector<Vector<Vector<Point>>>();
	    DBAdapter dba = new DBAdapter(a);
	   // Cursor c = dba.getGestures();
	   
	    try {
			dba.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    dba.openDataBase();
		Cursor c = dba.query();
        
		if (c.moveToFirst()) {

			while (c.isAfterLast() == false) {
				  dt.add((Vector<Vector<Point>>) Converter.byteToObject(c.getBlob(1)));
				  name.add(c.getString(0));
			    c.moveToNext();
			}
			
		}
		c.close();
		dba.close();
		
	}

	public native int DTW(Vector<Point> g1,Vector<Vector<Vector<Point>>> db);
	
	
	public String recognize(Vector<Point> g) {
		
		//for (int i = 0; i < dt.size(); i++) {
			//Vector<Vector<Point>> d = new Vector<Vector<Point>>();
			//d = dt.get(i);
			//for(int j =0; j < d.size() ; j++){
	           Log.v("Thread", "DTW_B");
			   int index= DTW(g,dt);
			   Log.v("Thread", "DTW_E");	
			  // }
		//	Log.v("Thread", "DTW" + name.get(i) + ":" + dist);
	 	
		if (index != -1)
			return name.get(index);
		else
			return "";
	}

	
}
