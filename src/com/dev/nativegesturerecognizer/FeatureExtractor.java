package com.dev.nativegesturerecognizer;

import java.io.Serializable;
import java.util.Vector;

import com.example.dtwgesturerecognizer.Point;

import android.util.Log;

public class FeatureExtractor implements Serializable {

	Gezture g = new Gezture();
	Vector<Point> data = new Vector<Point>();
	Vector<Point>  cdata;
	float[] mean = new float[4];
	float[] std = new float[4];
	float[] entropy = new float[4];
	float[] energy = new float[4];
	float[] kurt = new float[4];
	float[] skew = new float[4];
	float[] peak = new float[4];
	float[][] cov = new float[][]{{0,0,0},{0,0,0},{0,0,0}};

	public void setGesture(Gezture g2) {
		this.g = g2;
		this.data = g2.data;
		Log.v("fe",""+g2.data.get(0).x);
	}

	public void setGesture(Vector<Point> g2) {
		
		this.data = g2;
		
	}
	
	public float[] getMean() {
		float sumx = 0, sumy = 0, sumz = 0, sumr = 0;
		for (int i = 0; i < data.size(); i++) {
			sumx = sumx + data.get(i).x;
			sumy = sumy + data.get(i).y;
			sumz = sumz + data.get(i).z;
			sumr = sumr + data.get(i).r;
		}
		sumx = sumx / data.size();
		sumy = sumy / data.size();
		sumz = sumz / data.size();
		sumr = sumr / data.size();
		
		mean[0] = sumx;
		mean[1] = sumy;
		mean[2] = sumz;
		mean[3] = sumr;
		Log.v("Mean :", " "+mean[0]+","+mean[1]+","+mean[2]+","+data.get(0).x);
		return mean;
	}

	public float[] getStd() {
		float sumx = 0, sumy = 0, sumz = 0;
		for (int i = 0; i < data.size(); i++) {
			sumx = (float) (sumx + Math.pow((data.get(i).x - mean[0]), 2));
			sumy = (float) (sumy + Math.pow((data.get(i).y - mean[1]), 2));
			sumz = (float) (sumz + Math.pow((data.get(i).z - mean[2]), 2));
		}
		sumx = sumx / data.size();
		sumy = sumy / data.size();
		sumz = sumz / data.size();
		std[0] = (float) Math.sqrt(sumx);
		std[1] = (float) Math.sqrt(sumy);
		std[2] = (float) Math.sqrt(sumz);
		return std;
	}

	public float[] getEnergy() {
		float sumx = 0, sumy = 0, sumz = 0;
		for (int i = 0; i < data.size(); i++) {
			sumx = (float) (sumx + Math.pow(data.get(i).x, 2));
			sumy = (float) (sumy + Math.pow(data.get(i).y, 2));
			sumz = (float) (sumz + Math.pow(data.get(i).z, 2));
		}
		sumx = sumx / data.size();
		sumy = sumy / data.size();
		sumz = sumz / data.size();
		energy[0] = sumx;
		energy[1] = sumy;
		energy[2] = sumz;
		return energy;
	}

	public float[] getKurt() {
		float sumx = 0, sumy = 0, sumz = 0;
		for (int i = 0; i < data.size(); i++) {
			sumx = (float) (sumx + Math.pow((data.get(i).x - mean[0]), 4));
			sumy = (float) (sumy + Math.pow((data.get(i).y - mean[1]), 4));
			sumz = (float) (sumz + Math.pow((data.get(i).z - mean[2]), 4));
		}
		sumx = sumx / data.size();
		sumy = sumy / data.size();
		sumz = sumz / data.size();

		kurt[0] = (float) (sumx / Math.pow(std[0], 4));
		kurt[1] = (float) (sumy / Math.pow(std[1], 4));
		kurt[2] = (float) (sumz / Math.pow(std[2], 4));
		return kurt;
	}

	public float[] getSkew() {
		float sumx = 0, sumy = 0, sumz = 0;
		for (int i = 0; i < data.size(); i++) {
			sumx = (float) (sumx + Math.pow((data.get(i).x - mean[0]), 3));
			sumy = (float) (sumy + Math.pow((data.get(i).y - mean[1]), 3));
			sumz = (float) (sumz + Math.pow((data.get(i).z - mean[2]), 3));
		}
		sumx = sumx / data.size();
		sumy = sumy / data.size();
		sumz = sumz / data.size();

		skew[0] = (float) (sumx / Math.pow(std[0], 3));
		skew[1] = (float) (sumy / Math.pow(std[1], 3));
		skew[2] = (float) (sumz / Math.pow(std[2], 3));
		return skew;
	}

	public float[] getPeak() {
		float x = 0, y = 0, z = 0;
		for (int i = 1; i < data.size() - 1; i++) {
			if (data.get(i).x > data.get(i - 1).x
					&& data.get(i).x > data.get(i + 1).x) {
				x = x + 1;
			}
			if (data.get(i).y > data.get(i - 1).y
					&& data.get(i).y > data.get(i + 1).y) {
				y = y + 1;
			}
			if (data.get(i).z > data.get(i - 1).z
					&& data.get(i).z > data.get(i + 1).z) {
				z = z + 1;
			}
		}
		peak[0] = x;
		peak[1] = y;
		peak[2] = z;
		return peak;
	}

	
	public float[][] getCovMatrix(){
		getCentroidGezture();
		for (int i = 0; i < data.size(); i++) {
		cov[0][0] = cov[0][0] + ((cdata.get(i).x * cdata.get(i).x)/(cdata.size()-1));
		cov[0][1] = cov[0][1] + ((cdata.get(i).x * cdata.get(i).y)/(cdata.size()-1));
		cov[0][2] = cov[0][2] + ((cdata.get(i).x * cdata.get(i).z)/(cdata.size()-1));
		cov[1][0] = cov[1][0] + ((cdata.get(i).y * cdata.get(i).x)/(cdata.size()-1));
		cov[1][1] = cov[1][1] + ((cdata.get(i).y * cdata.get(i).y)/(cdata.size()-1));
		cov[1][2] = cov[1][1] + ((cdata.get(i).y * cdata.get(i).z)/(cdata.size()-1));
		cov[2][0] = cov[2][0] + ((cdata.get(i).z * cdata.get(i).x)/(cdata.size()-1));
		cov[2][1] = cov[2][1] + ((cdata.get(i).z * cdata.get(i).y)/(cdata.size()-1));
		cov[2][2] = cov[2][1] + ((cdata.get(i).z * cdata.get(i).z)/(cdata.size()-1));
		}
		return cov;
	}
	
	public Vector<Point> getCentroidGezture() {
	  getMean();
	  cdata = new Vector<Point>();
	  for (int i = 0; i < data.size(); i++) {
			Point pt = new Point();
			pt.x = data.get(i).x - mean[0];
			pt.y = data.get(i).y - mean[1];
			pt.z = data.get(i).z - mean[2];
			pt.r = data.get(i).r - mean[3];
			cdata.add(pt);
		}
		return cdata;
	}

	public void log() {
		String s1 = "MEAN              :" + getMean()[0] + "," + getMean()[1]
				+ "," + getMean()[2];
		String s2 = "STD_DEVIATION     :" + getStd()[0] + "," + getStd()[1] + ","
				+ getStd()[2];
		String s3 = "ENERGY            :" + getEnergy()[0] + "," + getEnergy()[1]
				+ "," + getEnergy()[2];
		String s4 = "KURTOSIS          :" + getKurt()[0] + "," + getKurt()[1]
				+ "," + getKurt()[2];
		String s5 = "SKEWNESS          :" + getSkew()[0] + "," + getSkew()[1]
				+ "," + getSkew()[2];
		String s6 = "NUMBER_OF_PEAKS   :" + getPeak()[0] + "," + getPeak()[1]
				+ "," + getPeak()[2];
		String s7 = "COVARIANCE MATRIX :" +getCovMatrix()[0][0] + " " +getCovMatrix()[0][1] + " " +getCovMatrix()[0][2];
		String s8 = "                   " +getCovMatrix()[1][0] + " " +getCovMatrix()[1][1] + " " +getCovMatrix()[1][2];
		String s9 = "                   " +getCovMatrix()[2][0] + " " +getCovMatrix()[2][1] + " " +getCovMatrix()[2][2];
		Log.v("FEATURE", s1 + "\n" + s2 + "\n" + s3 + "\n" + s4 + "\n" + s5
				+ "\n" + s6 + "\n"+s7+"\n"+s8+"\n"+s9+"\n");
		
	}
	
	
}