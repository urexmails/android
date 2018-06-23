package com.contactpoint.view.fragment.ci;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.util.Exchanger;
import com.contactpoint.model.util.MapMarker;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

//http://mobiforge.com/developing/story/using-google-maps-android
public class CustomerDetailsFragment extends SuperCIFragment {

	public static final String TAG = "CustomerDetailsFragment";
	
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
		// The Activity created the MapView for us, so we can do some init stuff.
		Exchanger.mMapView.setClickable(true);
		Exchanger.mMapView.setBuiltInZoomControls(true); // If you want.

		/*
		 * If you're getting Exceptions saying that the MapView already has
		 * a parent, uncomment the next lines of code, but I think that it
		 * won't be necessary. In other cases it was, but in this case I
		 * don't this should happen.
		 */
		/*
		 * final ViewGroup parent = (ViewGroup) Exchanger.mMapView.getParent();
		 * if (parent != null) parent.removeView(Exchanger.mMapView);
		 */
		
		View v = inflater.inflate(R.layout.ci_customer_detail, null);
		FrameLayout container = (FrameLayout) v.findViewById(R.id.map_container);
		container.addView(Exchanger.mMapView);
				
		// set texts
		((TextView)v.findViewById(R.id.txt_cust_name)).setText(
				getCIDownloadOutDTO().memberName.toString() + " H: " +
				getCIDownloadOutDTO().memberContact.phoneHome + " M: " +
				getCIDownloadOutDTO().memberContact.phoneMobile + " B: " +
				getCIDownloadOutDTO().memberContact.phoneBusiness);		
		((TextView)v.findViewById(R.id.txt_cust_add1)).setText(
				getCIDownloadOutDTO().memberAddress.streetAddress1 + " " + 
				getCIDownloadOutDTO().memberAddress.streetAddress2);		
		((TextView)v.findViewById(R.id.txt_cust_add2)).setText(
				getCIDownloadOutDTO().memberAddress.suburb + ", " +
				getCIDownloadOutDTO().memberAddress.state + " " +
				getCIDownloadOutDTO().memberAddress.postcode + ", " +
				getCIDownloadOutDTO().memberAddress.country);
		((TextView)v.findViewById(R.id.txt_pm_key)).setText(
				getResources().getString(R.string.ci_cust_detail_pm_key) + " " +
				getCIDownloadOutDTO().employeeCode);
		
		// view or hide sensitive text view
		if (getCIDownloadOutDTO().sensitive.compareTo("1") == 0) {
			v.findViewById(R.id.txt_sensitive).setVisibility(View.VISIBLE);
		} else {
			v.findViewById(R.id.txt_sensitive).setVisibility(View.GONE);
		}
		
		// add marker
		List<Overlay> mapOverlays = Exchanger.mMapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.map_marker);
		MapMarker itemizedoverlay = new MapMarker(drawable, getSherlockActivity());
		
		Geocoder geoCoder = new Geocoder(getSherlockActivity(), Locale.getDefault());
		GeoPoint point = null;
		MapController mc = Exchanger.mMapView.getController();
		try {
            List<Address> addresses = geoCoder.getFromLocationName(
            		getCIDownloadOutDTO().memberAddress.toString(), 5);
            if (addresses.size() > 0) {
            	point = new GeoPoint(
                        (int) (addresses.get(0).getLatitude() * 1E6), 
                        (int) (addresses.get(0).getLongitude() * 1E6));
                mc.animateTo(point);    
                Exchanger.mMapView.invalidate();
        		OverlayItem overlayitem = new OverlayItem(
        				point, 
        				getResources().getString(R.string.ci_cust_detail_contact), 
        				getResources().getString(R.string.ci_cust_detail_home) 		+ "\t" +
        				getCIDownloadOutDTO().memberContact.phoneHome 	+ "\n" +
        				getResources().getString(R.string.ci_cust_detail_business) 	+ "\t" +
        				getCIDownloadOutDTO().memberContact.phoneBusiness + "\n" +
        				getResources().getString(R.string.ci_cust_detail_mobile) 	+ "\t" +
        				getCIDownloadOutDTO().memberContact.phoneMobile 	+ "\n" +
                		getResources().getString(R.string.ci_cust_detail_fax) 		+ "\t" +
                		getCIDownloadOutDTO().memberContact.fax 			+ "\n" +
                        getResources().getString(R.string.ci_cust_detail_email) 	+ "\t" +
                        getCIDownloadOutDTO().memberContact.email);
        		itemizedoverlay.addOverlay(overlayitem);
        		mapOverlays.add(itemizedoverlay);
            }    
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return v;
	}	
}
