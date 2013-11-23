package com.example.dtwgesturerecognizer;

import java.io.Serializable;

public class Point implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7167931795970991893L;
	public float x;
	public float y;
	public float z;
	public float r;
	
	public Point(){
		
	}
	
	Point(float a, float b, float c, float d){
		this.x = a;
		this.y = b;
		this.z = c;
		this.r = d;
	}
	
}
