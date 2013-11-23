package com.dev.nativegesturerecognizer;

import java.util.Vector;

import com.example.dtwgesturerecognizer.Point;

public class DTW {

	
	//static int size = 31;
	static float[][] DTW ;

	
	
	public static float distance(Vector<Point> a,Vector<Point> b){
		DTW = new float[a.size()][b.size()];
		DTW[0][0]=0;
		for(int i=1; i < a.size(); i++)
			DTW[i][0]= Float.MAX_VALUE;
		for(int j=1; j < b.size(); j++)
			DTW[0][j]= Float.MAX_VALUE;
		
		for(int i=1; i < a.size(); i++){
			for(int j=1; j < b.size(); j++){
				
			 float cost = dist(a.get(i),b.get(j));
			 DTW[i][j] = cost + minimum(DTW[i-1][j],DTW[i][j-1],DTW[i-1][j-1]);
				
				
			}
		}
	
		return DTW[a.size()-1][b.size()-1];
	  }

	private static float minimum(float f, float g, float h) {
		// TODO Auto-generated method stub
		if(f<g && f<h)
			return f;
		else if(g<h)
			return g;
		else
			return h;
	   }

public static float distanceI(Vector<Integer> a,Vector<Integer> b){
	    
	    DTW = new float[a.size()][b.size()];
		DTW[0][0]=0;
		for(int i=1; i < a.size(); i++)
			DTW[i][0]= Float.MAX_VALUE;
		for(int j=1; j < b.size(); j++)
			DTW[0][j]= Float.MAX_VALUE;
		
		for(int i=1; i < a.size(); i++){
			for(int j=1; j < b.size(); j++){
				
			 float cost = Math.abs(a.get(i)-b.get(j));
			 DTW[i][j] = cost + minimum(DTW[i-1][j],DTW[i][j-1],DTW[i-1][j-1]);
				
				
			}
		}
	
		return DTW[a.size()-1][b.size()-1];
	  }
	
	private static float dist(Point cpoint, Point point) {
		float d = 0;
		d = d + (cpoint.x - point.x) * (cpoint.x - point.x);
		d = d + (cpoint.y - point.y) * (cpoint.y - point.y);
		d = d + (cpoint.z - point.z) * (cpoint.z - point.z);
		//d = d + (cpoint.r - point.r) * (cpoint.r - point.r);
		d = (float) Math.sqrt(d);
		return d;
	
	}

	
}


