package com.contactpoint.model.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;

import android.os.Environment;

public class ExternalIO {

	private static final String TOLL_FOLDER = "/Toll App Log/";
	//private static final String NOT_MOUNTED = "Unable to find SD-Card: SD-Card is not mounted.";
	
	public static void saveToExternalStorage(String filename, Serializable param) {

		/*
		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		}*/
		
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			/*
			AlertDialog ad = DialogFactory.getAlertDialog(context);
			ad.setMessage(NOT_MOUNTED);
			ad.show();*/
			return;
		}
		
		File folder = new File(Environment.getExternalStorageDirectory(), TOLL_FOLDER);
		folder.mkdir();			
		File file = new File(folder, filename);
		
		try {
			file.createNewFile();
            FileOutputStream f = new FileOutputStream(file);
			OutputStreamWriter w = new OutputStreamWriter(f);
			if (param != null) w.write(param.toString());
			else w.write("null");
			w.close();
            f.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			if (file.exists()) file.delete();
		}
	}
}
