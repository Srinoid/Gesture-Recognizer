package com.dev.nativegesturerecognizer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

public class Converter {
	
	public static  <T> byte[] toByte(T object) {

		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		try {
			ObjectOutputStream obs = new ObjectOutputStream(bs);
			obs.writeObject(object);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bs.toByteArray();
	}
	
	public static  <T> Object byteToObject(byte[] data){ 
	
	    Object b=null;
		ObjectInputStream in=null;
		try {
			in = new ObjectInputStream(new ByteArrayInputStream(data));
		} catch (StreamCorruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		try {
			 b =  in.readObject();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return b;
	}
}
