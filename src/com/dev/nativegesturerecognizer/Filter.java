package com.dev.nativegesturerecognizer;

import com.example.dtwgesturerecognizer.Point;

import android.util.Log;



public class Filter {

	
public static Gezture smoothFilter(Gezture iGesture, float alpha) {
		
		Gezture oGesture = new Gezture();
		
		Point p = new Point();
		p.x=iGesture.get(0).x;
		p.y=iGesture.get(0).y;
		p.z=iGesture.get(0).z;
		p.r=iGesture.get(0).r;
		oGesture.add(p);
		
		Log.v("VECTOR",""+iGesture.data.size());
		
		for(int i=1; i < iGesture.data.size(); i++){
		    
			Point p1 = new Point();
			p1.x = oGesture.get(i-1).x + alpha * (iGesture.get(i).x  - oGesture.get(i-1).x);
			p1.y = oGesture.get(i-1).y + alpha * (iGesture.get(i).y  - oGesture.get(i-1).y);
			p1.z = oGesture.get(i-1).z + alpha * (iGesture.get(i).z  - oGesture.get(i-1).z);
			p1.r = oGesture.get(i-1).r + alpha * (iGesture.get(i).r  - oGesture.get(i-1).r);
			oGesture.add(p1);
		}
		return oGesture;
	}
	
  public static Gezture amp(Gezture iGesture, float kfactor) {
	Gezture oGesture = new Gezture();
	for(int i=0; i < iGesture.data.size(); i++){
	Point p = new Point();
	p.x = kfactor *iGesture.get(i).x;
	p.y = kfactor *iGesture.get(i).y;
	p.z = kfactor *iGesture.get(i).z;
	p.r = kfactor *iGesture.get(i).r;
	oGesture.add(p);
	}
	return oGesture;
  }
	
	public static Gezture hpfFilter(Gezture iGesture, float kfactor) {
		Gezture oGesture = new Gezture();
		Point p = new Point();
		p.x = iGesture.get(0).x;
		p.y = iGesture.get(0).y;
		p.z = iGesture.get(0).z;
		p.r = iGesture.get(0).r;
		oGesture.add(p);
		for (int i = 1; i < iGesture.data.size(); i++) {
            Point p1 = new Point();
			p1.x = kfactor
					* (oGesture.get(i - 1).x + iGesture.get(i).x - iGesture.get(i - 1).x);
			p1.y = kfactor
					* (oGesture.get(i - 1).y + iGesture.get(i).y - iGesture.get(i - 1).y);
			p1.z = kfactor
					* (oGesture.get(i - 1).z + iGesture.get(i).z - iGesture.get(i - 1).z);
			p1.r = kfactor
					* (oGesture.get(i - 1).r + iGesture.get(i).r - iGesture.get(i - 1).r);
			oGesture.add(p1);
		}
		
		return oGesture;
	}

	

	public static Gezture thresholdFilter(Gezture iGesture, float epsilon) {
		Gezture oGesture = new Gezture();
		
		Point value = new Point();
		Point prev = new Point();
		
		prev.x = 0;
		prev.y = 0;
		prev.z = 0;
		prev.r = 0;
		
		for(int i=0; i < iGesture.data.size(); i++){ 
			
			value = iGesture.get(i);
			Point p = new Point();
			if (Math.abs(value.x - prev.x) > epsilon)
				p.x=value.x;
			else
				p.x=prev.x;
			if (Math.abs(value.y - prev.y) > epsilon)
				p.y=value.y;
			else
				p.y=prev.y;
			if (Math.abs(value.z - prev.z) > epsilon)
				p.z=value.z;
			else
				p.z=prev.z;
			if (Math.abs(value.r - prev.r) > epsilon)
				p.r=value.r;
			else
				p.r=prev.r;		 
				oGesture.add(p);
			    prev = p;
		}
		Log.v("VECTOR",""+oGesture.data.size());
		return oGesture;
	}

	public static Gezture movingAvgFiter(Gezture iGesture, int length) {
		int limit = iGesture.data.size();
		Gezture oGesture = new Gezture();

		for (int i = 0; i < limit; i++) {
			float sumx = 0, sumy = 0, sumz = 0, sumr=0;
			for (int j = i - (length - 1) / 2; j <= i + (length - 1) / 2; j++) {
				if ((j >= 0) && (j < limit)) {
					sumx = sumx + iGesture.get(j).x;
					sumy = sumy + iGesture.get(j).y;
					sumz = sumz + iGesture.get(j).z;
					sumr = sumr + iGesture.get(j).r;
				}
			}

			Point p = new Point();
			p.x = (float) sumx / length;
			p.y = (float) sumy / length;
			p.z = (float) sumz / length;
			p.r = (float) sumr / length;
			oGesture.add(p);

		}
		oGesture.timestamp = iGesture.timestamp;
		return oGesture;
	}

	public static Gezture normalize(Gezture iGesture){
		float M;
		float X,Y,Z,R;
		Gezture oGesture = new Gezture();
		for(int i=1; i < iGesture.data.size(); i++){ 
		
			X=iGesture.get(i).x;
			Y=iGesture.get(i).y;
			Z=iGesture.get(i).z;
			R=iGesture.get(i).r;
			
			M=(float) Math.sqrt(X*X+Y*Y+Z*Z);
			Point p = new Point();
			p.x = 10*X/M;
			p.y = 10*Y/M;
			p.z = 10*Z/M;
			p.r = R;
		    oGesture.add(p);
		
	  }
		return oGesture;
    }
	
}
