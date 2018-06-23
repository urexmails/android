package com.contactpoint.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kobjects.base64.Base64;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.PhotoInDTO;
import com.contactpoint.model.photo.FroyoAlbumDirFactory;
import com.contactpoint.model.util.DialogFactory;

public class PhotoController {

	public enum PhotoCategory {
		ACCESS, COLLECTION, SUMMARY, GENERAL, INTERNAL,
		UPLIFT, DELIVERY
	}
	
	private final static int IMG_WIDTH = 800;
	private final static int IMG_HEIGHT = 600;
	private final static int QUALITY = 30;
	private final static int DPI = 72;

	private PhotoInDTO inDTO;
	private AlertDialog mCommentDialog;
	private Activity mActivity;

	public final int PHOTO_REQUEST_CODE = 101;		// random number

	// preview
	private AlertDialog mPreviewDialog;
	private ImageView mPreviewImageView;

	// constants
	public final static String APP_TYPE_CI 	 = "CI";
	public final static String APP_TYPE_PRV  = "PRV";

	private final String DESC_ACCESS 	 = "ACCESS";
	private final String DESC_SUMMARY 	 = "ROOM SUMMARY";
	private final String DESC_GENERAL 	 = "GENERAL COMMENTS";
	private final String DESC_INTERNAL 	 = "INTERNAL COMMENTS";
	private final String DESC_COLLECTION = "ITEM COLLECTION";
	private final String DESC_UPLIFT 	 = "UPLIFT";
	private final String DESC_DELIVERY 	 = "DELIVERY";

	private String mCurrentPhotoPath;

	private final String JPEG_FILE_PREFIX = "IMG_";
	private final String JPEG_FILE_SUFFIX = ".jpg";

	private final String FEEDBACK_FORMAT = "%d Picture(s) selected from maximum of %d";

	private FroyoAlbumDirFactory mAlbumStorageDirFactory = null;

	public PhotoController(Activity activity) {
		inDTO = new PhotoInDTO();
		mActivity = activity;

		// use prv dialog incomplete as a dialog with an edit text
		mCommentDialog = CommonController.dialogFromBuilder(mActivity, 
				R.layout.prv_dialog_incomplete, android.R.style.Theme_Holo_Light_Dialog);
		mCommentDialog.setTitle(mActivity.getResources().getString(R.string.photo_comment_title));
		mCommentDialog.setButton(DialogInterface.BUTTON_POSITIVE, 
				mActivity.getResources().getString(R.string.btn_confirm), mCommentListener);
		mCommentDialog.setButton(DialogInterface.BUTTON_NEGATIVE, 
				mActivity.getResources().getString(R.string.btn_cancel), mCommentListener);
		
		// preview dialog
		mPreviewDialog = CommonController.dialogFromBuilder(mActivity, 
				R.layout.photo_dialog_preview, android.R.style.Theme_Holo_Light_Dialog);
		mPreviewDialog.setTitle(mActivity.getResources().getString(R.string.photo_preview_title));
		mPreviewDialog.setButton(DialogInterface.BUTTON_POSITIVE, 
				mActivity.getResources().getString(R.string.btn_confirm), mPreviewListener);
		mPreviewDialog.setButton(DialogInterface.BUTTON_NEGATIVE, 
				mActivity.getResources().getString(R.string.btn_cancel), mPreviewListener);
		mPreviewDialog.setButton(DialogInterface.BUTTON_NEUTRAL, 
				mActivity.getResources().getString(R.string.btn_preview_take_another), mPreviewListener);
		
		mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		// if not camera or gallery picker
		if (requestCode != PHOTO_REQUEST_CODE) return;

		final boolean isCamera = data == null || MediaStore.ACTION_IMAGE_CAPTURE.equals(data.getAction());

		if(resultCode != Activity.RESULT_OK)
		{
			deletePicture(mActivity, mCurrentPhotoPath);
			return;
		}

		if (isCamera) {
			if (mCurrentPhotoPath != null) {
				//setPic();
				mPreviewDialog.show();
				mPreviewImageView = (ImageView)mPreviewDialog.findViewById(R.id.photo_preview_image);				
				mPreviewImageView.setImageURI(Uri.fromFile(new File(mCurrentPhotoPath)));
			}
		} else {
			deletePicture(mActivity, mCurrentPhotoPath);
			String path = getRealPathFromURI(mActivity, data.getData());
			inDTO.setPhoto(path);
			mCurrentPhotoPath = null;
			mCommentDialog.show();
		}

		// http://stackoverflow.com/questions/4455558/allow-user-to-select-camera-or-gallery-for-image
	}

	private DialogInterface.OnClickListener mCommentListener = 
			new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			EditText commentText = (EditText)mCommentDialog.findViewById(R.id.prv_incomplete_reason);
			if (which == DialogInterface.BUTTON_POSITIVE) {
				inDTO.setComment(commentText.getText().toString());
				commentText.setText("");
			}

			/*
			System.out.println(inDTO.getComment());
			System.out.println(inDTO.getAppType());
			System.out.println(inDTO.getCategory());
			System.out.println(inDTO.getPONumber());
			System.out.println(inDTO.getPhoto());*/

			ModelFactory.getPhotoService().addPhotoInDTO(inDTO);
			ModelFactory.getPhotoService().saveFormData(mActivity);

			String messsage = String.format(FEEDBACK_FORMAT,
					ModelFactory.getPhotoService().getTotalPhoto(inDTO.getPONumber()),
					ModelFactory.getPhotoService().getPhotoLimit(inDTO.getAppType()));

			AlertDialog ad = DialogFactory.getAlertDialog(mActivity);
			ad.setMessage(messsage);
			ad.show();

			// prevent previous DTO changed when user adds new photo
			inDTO = new PhotoInDTO();
		}
	};
	
	private DialogInterface.OnClickListener mPreviewListener = 
			new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				inDTO.setPhoto(mCurrentPhotoPath);
				galleryAddPic();
				mCommentDialog.show();
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				deletePicture(mActivity, mCurrentPhotoPath);
				mCurrentPhotoPath = null;
				break;
			case DialogInterface.BUTTON_NEUTRAL:
				deletePicture(mActivity, mCurrentPhotoPath);
				File f = null;

				try {
					f = setUpPhotoFile();
					mCurrentPhotoPath = f.getAbsolutePath();
				} catch (IOException e) {
					e.printStackTrace();
					f = null;
					mCurrentPhotoPath = null;
				}
				
				// Camera.
				final List<Intent> cameraIntents = new ArrayList<Intent>();
				final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				final PackageManager packageManager = mActivity.getPackageManager();
				final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
				for(ResolveInfo res : listCam) {
					final String packageName = res.activityInfo.packageName;
					final Intent intent = new Intent(captureIntent);
					intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
					intent.setPackage(packageName);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					cameraIntents.add(intent);
				}

				// Filesystem.
				final Intent galleryIntent = new Intent();
				galleryIntent.setType("image/*");
				galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

				// Chooser of filesystem options.
				final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

				// Add the camera options.
				chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));

				mActivity.startActivityForResult(chooserIntent, PHOTO_REQUEST_CODE);				
				break;
			}
		}
	};

	public void onPhotoMenuSelected(PhotoCategory category, String poNumber, boolean isMM) {
		if (!isIntentAvailable(mActivity, MediaStore.ACTION_IMAGE_CAPTURE)) {
			AlertDialog ad = DialogFactory.getAlertDialog(mActivity);
			ad.setMessage(mActivity.getString(R.string.alert_no_camera));
			ad.show();
			return;
		}

		inDTO.setPONumber(poNumber);
		inDTO.setComment("");
		inDTO.setIsMM(isMM);

		switch (category) {
		case ACCESS:
			inDTO.setAppType(APP_TYPE_PRV);
			inDTO.setCategory(DESC_ACCESS);
			break;
		case COLLECTION:
			inDTO.setAppType(APP_TYPE_PRV);
			inDTO.setCategory(DESC_COLLECTION);
			break;
		case SUMMARY:
			inDTO.setAppType(APP_TYPE_PRV);
			inDTO.setCategory(DESC_SUMMARY);
			break;
		case GENERAL:
			inDTO.setAppType(APP_TYPE_PRV);
			inDTO.setCategory(DESC_GENERAL);
			break;
		case INTERNAL:
			inDTO.setAppType(APP_TYPE_PRV);
			inDTO.setCategory(DESC_INTERNAL);
			break;
		case UPLIFT:
			inDTO.setAppType(APP_TYPE_CI);
			inDTO.setCategory(DESC_UPLIFT);
			break;
		case DELIVERY:
			inDTO.setAppType(APP_TYPE_CI);
			inDTO.setCategory(DESC_DELIVERY);
			break;
		}

		if (ModelFactory.getPhotoService().getTotalPhoto(poNumber) == ModelFactory.getPhotoService().getPhotoLimit(inDTO.getAppType())) {
			AlertDialog ad = DialogFactory.getAlertDialog(mActivity);
			ad.setMessage(mActivity.getString(R.string.alert_reach_limit));
			ad.show();
			return;
		}


		File f = null;

		try {
			f = setUpPhotoFile();
			mCurrentPhotoPath = f.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
			f = null;
			mCurrentPhotoPath = null;
		}

		/*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		mActivity.startActivityForResult(takePictureIntent, PHOTO_REQUEST_CODE);*/

		// Camera.
		final List<Intent> cameraIntents = new ArrayList<Intent>();
		final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		final PackageManager packageManager = mActivity.getPackageManager();
		final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
		for(ResolveInfo res : listCam) {
			final String packageName = res.activityInfo.packageName;
			final Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
			intent.setPackage(packageName);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			cameraIntents.add(intent);
		}

		// Filesystem.
		final Intent galleryIntent = new Intent();
		galleryIntent.setType("image/*");
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

		// Chooser of filesystem options.
		final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

		// Add the camera options.
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));

		mActivity.startActivityForResult(chooserIntent, PHOTO_REQUEST_CODE);
	}

	private File setUpPhotoFile() throws IOException {

		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();

		return f;
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + inDTO.getPONumber()  + "_" + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}

	private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (! storageDir.mkdirs()) {
					if (! storageDir.exists()){
						Log.d("TollTransitionsApp", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(mActivity.getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}

	/* Photo album for this application */
	private String getAlbumName() {
		return mActivity.getString(R.string.album_name);
	}

	public static String getStringPic(String path) {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		try {
			/* Get the size of the ImageView */
			int targetW = IMG_WIDTH;
			int targetH = IMG_HEIGHT;
	
			/* Get the size of the image */
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, bmOptions);
			int photoW = bmOptions.outWidth;
			int photoH = bmOptions.outHeight;
	
			/* Figure out which way needs to be reduced less */
			int scaleFactor = 1;
	
			if (photoW > targetW || photoH > targetH) {
				scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
			}
	
			/* Set bitmap options to scale the image decode target */
			bmOptions.inJustDecodeBounds = false;
			bmOptions.inSampleSize = scaleFactor;
			bmOptions.inPurgeable = true;
			bmOptions.inDensity = DPI;
			bmOptions.inPreferredConfig = Config.RGB_565;
			bmOptions.inDither = true;
	
			/* Decode the JPEG file into a Bitmap */
			Bitmap image = BitmapFactory.decodeFile(path, bmOptions);
	
			//System.out.println("Ori Frame size: " + photoW + " " + photoH);
			//System.out.println(path);
	
			if (image == null) {
				return "";
			}
			//System.out.println("Frame size: " + image.getWidth() + " " + image.getHeight());
			//System.out.println("File Size: " + image.getByteCount());
			//System.out.println("DPI: " + image.getDensity());
			if (image.getWidth() > targetW || image.getHeight() > targetH) {
				image = Bitmap.createScaledBitmap(image, targetW, targetH, false);
			}
	
	
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, QUALITY, stream);
			byte[] byteArray = stream.toByteArray();
			image.recycle();
	
			return Base64.encode(byteArray);
		} catch (Exception e) {
			// Hyap #8939 - return empty string on exception, i.e. OutOfMemory error
			// The OutOfMemory error reported in this ticket comes from BitmapFactory.decodeFile method, 
			// which is an Android Library. 
			return "";
		}
	}

	public static void deletePicture(Context context, String path) {
		File file = new File(path);
		file.delete();
		
		// refresh
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri contentUri = Uri.parse("file://" + Environment.getExternalStorageDirectory());
		mediaScanIntent.setData(contentUri);		
		context.sendBroadcast(mediaScanIntent);
	}

	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		mActivity.sendBroadcast(mediaScanIntent);
	}

	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
	 *
	 * @param context The application's environment.
	 * @param action The Intent action to check for availability.
	 *
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list =
				packageManager.queryIntentActivities(intent,
						PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	public String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try { 
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
}
