package com.dev.nativegesturerecognizer;

import com.example.dtwgesturerecognizer.Point;

public class Scaler {

	protected static int[] group;

	public static void groupSize(Gezture iGesture, int length) {
        
		group = new int[1];
		group[0] = iGesture.data.size();
		int size = 1;

		while (size <= length / 2) {
			doubler();
			size = size * 2;
		}

	}

	public static Gezture deOffset(Gezture iGesture) {
		Gezture oGesture =new Gezture();
		float offx = iGesture.data.get(0).x;
		float offy = iGesture.data.get(0).y;
		float offz = iGesture.data.get(0).z;
		for (int i = 0; i < iGesture.data.size(); i++) {
			Point p = new Point();
			p.x = iGesture.data.get(i).x-offx;
			p.y = iGesture.data.get(i).y-offy;
			p.z = iGesture.data.get(i).z-offz;
			oGesture.data.add(p);	
		}
		return oGesture;
	}
	
	
	public static Gezture  boundBox(Gezture iGesture, int length) {
       // Gezture gest =deOffset(iGesture);
		Gezture oGesture = clusterize(iGesture, length);
		return oGesture;
	}

	public static Gezture clusterize(Gezture iGesture, int length) {
		Gezture oGesture = new Gezture();
		int min = 0;
		int max = 0;
		groupSize(iGesture, length);
		for (int i = 0; i < group.length; i++) {
			min = max;
			max = max + group[i];
			float sumx = 0, sumy = 0, sumz = 0, sumr = 0;
			for (int j = min; j < max; j++) {
				// int j =(int) Math.floor((min+max)/2);
				sumx = sumx + iGesture.data.get(j).x;
				sumy = sumy + iGesture.data.get(j).y;
				sumz = sumz + iGesture.data.get(j).z;
				sumr = sumr + iGesture.data.get(j).r;
			}
			sumx = (float) sumx / (max - min);
			sumy = (float) sumy / (max - min);
			sumz = (float) sumz / (max - min);
            
			Point p = new Point();
			p.x = sumx; 
			p.y = sumy;
			p.z = sumz;
			p.r = sumr;
			oGesture.data.add(p);	
		}
		return oGesture;
	}

	public static void doubler() {
		int[] g = new int[group.length];
		for (int k = 0; k < group.length; k++) {
			g[k] = group[k];
		}
		group = new int[g.length * 2];
		for (int i = 0; i < 2 * g.length; i += 2) {
			group[i] = (int) Math.floor((float) (g[i / 2]) / 2);
			group[i + 1] = (int) Math.ceil((float) (g[i / 2]) / 2);
		}
	}
	
	
	
}
