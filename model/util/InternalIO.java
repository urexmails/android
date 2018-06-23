package com.contactpoint.model.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.Gson;

public class InternalIO {

	public static final String PRV_FORM = "toll_prv_form.dat";
	public static final String UPLIFT_FORM = "toll_uplift_form.dat";
	public static final String DELIVERY_FORM = "toll_delivery_form.dat";
	public static final String REF_FORM = "toll_reference_data.dat";
	public static final String REF_MM_FORM = "toll_mm_reference_data.dat";
	public static final String PHOTO = "toll_photo.dat";
	
	private static final String JSON = "json_";
	private static final String TEMP = "temp_";
	
	public static void saveToInternalStorage(Context context, String filename, Serializable param) {
		OutputStreamWriter osw = null;
		FileOutputStream fileOut = null;
		try {

            fileOut = context.openFileOutput(TEMP + JSON + filename, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = gson.toJson(param);
            osw = new OutputStreamWriter(fileOut);
            osw.write(json);
            osw.close();

            File originalFile = context.getFileStreamPath(TEMP + JSON + filename);
            File newFile = new File(originalFile.getParent(), JSON + filename);
            if (newFile.exists()) {
            	context.deleteFile(JSON + filename);
            }
            originalFile.renameTo(newFile);
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (osw != null) { try { osw.close(); } catch (IOException e) { e.printStackTrace(); } }
            if (fileOut != null) { try { fileOut.close(); } catch (IOException e) { e.printStackTrace(); } }
        }
		System.gc();
	}
	
	public static <T> T loadFromInternalStorage(Context context, String filename, Type type) {
        T object = null;
        InputStream fileIn = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fileIn = context.openFileInput(JSON + filename);

            if (fileIn != null) {
            	isr = new InputStreamReader(fileIn);
            	br = new BufferedReader(isr);
            	
            	String json = "";
            	StringBuilder sb = new StringBuilder();
            	
            	while((json = br.readLine()) != null) {
            		sb.append(json);
            	}
            	            	
            	json = sb.toString();
                Gson gson = new Gson();
                object = gson.fromJson(json, type);
            }

        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) { try { br.close(); } catch (IOException e) { e.printStackTrace(); } }
            if (isr != null) { try { isr.close(); } catch (IOException e) { e.printStackTrace(); } }
            if (fileIn != null) { try { fileIn.close(); } catch (IOException e) { e.printStackTrace(); } }
        }

        return object;
	}
	
	public static void resetFiles(Context context) {		
		
		context.deleteFile(JSON + PRV_FORM);
		context.deleteFile(JSON + UPLIFT_FORM);
		context.deleteFile(JSON + DELIVERY_FORM);
		context.deleteFile(JSON + REF_FORM);
		context.deleteFile(JSON + REF_MM_FORM);
		context.deleteFile(JSON + PHOTO);
	}
}
