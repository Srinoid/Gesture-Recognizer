package com.dev.nativegesturerecognizer;

import java.util.Vector;

import com.example.dtwgesturerecognizer.Point;

public class Gezture {

	public Vector<Point> data = new Vector<Point>();
	public Vector<Float> timestamp = new Vector<Float>();
	public String label;
	
	
	Gezture(){
		
	}

	public Gezture(Vector<Point> vector) {
		// TODO Auto-generated constructor stub
		this.data = vector;
	}

	public void add(Point p){
		data.add(p);
	}
	
	public Point get(int i){
		return data.get(i);
	}
	
	
	public int size(){
		return data.size();
	}
	
	public Vector<Point> getData(){
		return data;
	}

	public void setData(Vector<Point> d){
	   data=d;
	}
}
