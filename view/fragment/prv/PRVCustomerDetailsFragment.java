package com.contactpoint.view.fragment.prv;

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

import com.actionbarsherlock.app.SherlockFragment;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.client.prv.PRVDownload;
import com.contactpoint.model.util.Exchanger;
import com.contactpoint.model.util.MapMarker;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class PRVCustomerDetailsFragment extends SherlockFragment {

	public static final String TAG = "PRVCustomerDetailsFragment";

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
		 * final ViewGroup parent = (ViewGroup) Exchanger.mMapView2.getParent();
		 * if (parent != null) parent.removeView(Exchanger.mMapView2);
		 */

		View v = inflater.inflate(R.layout.prv_customer_detail, null);
		FrameLayout container = (FrameLayout) v.findViewById(R.id.map_container);
		container.addView(Exchanger.mMapView);

		PRVDownload outDTO = ModelFactory.getPRVService().getCurrentForm().getDownload().get(0);
		// set texts
		((TextView)v.findViewById(R.id.prv_cust_name_txt)).setText(outDTO.memberName.toString());
		((TextView)v.findViewById(R.id.prv_emp_code_text)).setText(outDTO.employeeCode);
		((TextView)v.findViewById(R.id.prv_bh_text)).setText(outDTO.memberContact.phoneBusiness);
		((TextView)v.findViewById(R.id.prv_ah_text)).setText(outDTO.memberContact.phoneHome);
		((TextView)v.findViewById(R.id.prv_m_text)).setText(outDTO.memberContact.phoneMobile);
		((TextView)v.findViewById(R.id.prv_email_text)).setText(outDTO.memberContact.email);
		((TextView)v.findViewById(R.id.prv_address_text)).setText(outDTO.memberAddress.toString());

		// view or hide sensitive text view
		if (ModelFactory.getUtilService().isSensitive(outDTO.sensitive)) {
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
					outDTO.memberAddress.toString(), 5);
			if (addresses.size() > 0) {
				point = new GeoPoint(
						(int) (addresses.get(0).getLatitude() * 1E6), 
						(int) (addresses.get(0).getLongitude() * 1E6));
				mc.animateTo(point);    
				Exchanger.mMapView.invalidate();
//				Exchanger.mMapView.getController().setZoom(12);
				OverlayItem overlayitem = new OverlayItem(
						point, 
						getResources().getString(R.string.ci_cust_detail_contact), 
						getResources().getString(R.string.ci_cust_detail_home) 		+ "\t" +
								outDTO.memberContact.phoneHome 	+ "\n" +
								getResources().getString(R.string.ci_cust_detail_business) 	+ "\t" +
								outDTO.memberContact.phoneBusiness + "\n" +
								getResources().getString(R.string.ci_cust_detail_mobile) 	+ "\t" +
								outDTO.memberContact.phoneMobile 	+ "\n" +
								getResources().getString(R.string.ci_cust_detail_fax) 		+ "\t" +
								outDTO.memberContact.fax 			+ "\n" +
								getResources().getString(R.string.ci_cust_detail_email) 	+ "\t" +
								outDTO.memberContact.email);
				itemizedoverlay.addOverlay(overlayitem);
				mapOverlays.add(itemizedoverlay);
			}    
		} catch (IOException e) {
			e.printStackTrace();
		}

		return v;
	}
}
