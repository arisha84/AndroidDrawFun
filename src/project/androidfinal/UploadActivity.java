package project.androidfinal;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.xml.sax.ext.Locator2Impl;
import org.xml.sax.helpers.LocatorImpl;

import project.androidfinal.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UploadActivity extends Activity {
	private LocationManager locationMgr;
	private TextView tv;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        HandleLocation();
        
        
        // handle picture preview
        Intent intent = this.getIntent();
        String uriStr = intent.getExtras().getString("picUri");
        Uri uri = Uri.parse(uriStr);
        try {
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
			 ImageView imV = (ImageView) findViewById(R.id.imageViewPic);
		     imV.setImageBitmap(bitmap);
		        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
       
       
    }
	
	private void HandleLocation() {
			tv = (TextView) findViewById(R.id.textViewLoction);
			locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			String locationProvider = locationMgr.getBestProvider(criteria, true);
			Location location = locationMgr.getLastKnownLocation(locationProvider);
			if (location == null)
				tv.setText("Unknown Location");
			else
				tv.setText("lat: "+ location.getLatitude() + "\nlong: " + location.getLongitude());
		
	}

	public void myClickHandle (View v)
    {
		// TODO: implement in the future
		this.finish();
    }
	

}
