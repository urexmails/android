package com.contactpoint.view;

import java.io.ByteArrayOutputStream;

import org.kobjects.base64.Base64;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

//http://corner.squareup.com/2010/07/smooth-signatures.html
public class SignatureView extends View {

	private static final float STROKE_WIDTH = 5f;

	/** Need to track this so the dirty region can accommodate the stroke. **/
	private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;

	private Paint paint = new Paint();
	private Path path = new Path();
	private Bitmap oldSignature = null;

	/**
	 * Optimizes painting by invalidating the smallest possible area.
	 */
	private float lastTouchX;
	private float lastTouchY;
	private final RectF dirtyRect = new RectF();

	public SignatureView(Context context, AttributeSet attrs) {
		super(context, attrs);

		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeWidth(STROKE_WIDTH);
	}

	/**
	 * Erases the signature.
	 */
	public void clear() {
		path.reset();
		if (oldSignature != null) {
			oldSignature.recycle();
			oldSignature = null;
		}

		// Repaints the entire view.
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (oldSignature == null) {
			canvas.drawPath(path, paint);
		} else {
			canvas.drawBitmap(oldSignature, 0, 0, null);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled()) return true;

		float eventX = event.getX();
		float eventY = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			path.moveTo(eventX, eventY);
			lastTouchX = eventX;
			lastTouchY = eventY;
			// There is no end point yet, so don't waste cycles invalidating.
			return true;

		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_UP:
			// Start tracking the dirty region.
			resetDirtyRect(eventX, eventY);

			// When the hardware tracks events faster than they are delivered, the
			// event will contain a history of those skipped points.
			int historySize = event.getHistorySize();
			for (int i = 0; i < historySize; i++) {
				float historicalX = event.getHistoricalX(i);
				float historicalY = event.getHistoricalY(i);
				expandDirtyRect(historicalX, historicalY);
				path.lineTo(historicalX, historicalY);
			}

			// After replaying history, connect the line to the touch point.
			path.lineTo(eventX, eventY);
			break;

		default:
			System.out.println("Ignored touch event: " + event.toString());
			return false;
		}

		// Include half the stroke width to avoid clipping.
		invalidate(
				(int) (dirtyRect.left - HALF_STROKE_WIDTH),
				(int) (dirtyRect.top - HALF_STROKE_WIDTH),
				(int) (dirtyRect.right + HALF_STROKE_WIDTH),
				(int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

		lastTouchX = eventX;
		lastTouchY = eventY;

		return true;
	}

	/**
	 * Called when replaying history to ensure the dirty region includes all
	 * points.
	 */
	private void expandDirtyRect(float historicalX, float historicalY) {
		if (historicalX < dirtyRect.left) {
			dirtyRect.left = historicalX;
		} else if (historicalX > dirtyRect.right) {
			dirtyRect.right = historicalX;
		}
		if (historicalY < dirtyRect.top) {
			dirtyRect.top = historicalY;
		} else if (historicalY > dirtyRect.bottom) {
			dirtyRect.bottom = historicalY;
		}
	}

	/**
	 * Resets the dirty region when the motion event occurs.
	 */
	private void resetDirtyRect(float eventX, float eventY) {

		// The lastTouchX and lastTouchY were set when the ACTION_DOWN
		// motion event occurred.
		dirtyRect.left 	 = Math.min(lastTouchX, eventX);
		dirtyRect.right  = Math.max(lastTouchX, eventX);
		dirtyRect.top 	 = Math.min(lastTouchY, eventY);
		dirtyRect.bottom = Math.max(lastTouchY, eventY);
	}

	// save signature as base64
	public String saveAsBase64String() {
		if (path.isEmpty() && oldSignature == null) return null;
		
		Bitmap signature = Bitmap.createBitmap(getWidth(), getHeight(), 
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(signature);
		draw(c);
		//System.out.println("Width: " + getWidth() + "Height: " + getHeight());
		//signature = getResizedBitmap(signature, 100, 100);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		signature.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		signature.recycle();
		return Base64.encode(byteArray);
	}
	
	public void loadBase64String(String savedString) {
		byte[] imageAsBytes = Base64.decode(savedString);
		if (oldSignature != null) {
			oldSignature.recycle();
			oldSignature = null;
		}
		oldSignature = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
		//oldSignature = getResizedBitmap(oldSignature, 980, 400);
		invalidate();
	}
}



